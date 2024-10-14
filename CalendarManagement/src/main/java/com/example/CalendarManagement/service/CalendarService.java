package com.example.CalendarManagement.service;

import com.example.CalendarManagement.exception.CannotBookMeetingWithYourselfException;
import com.example.CalendarManagement.exception.MeetingConflictException;
import com.example.CalendarManagement.model.Employee;
import com.example.CalendarManagement.model.Meeting;
import com.example.CalendarManagement.model.TimeSlot;
import com.example.CalendarManagement.repository.EmployeeRepository;
import com.example.CalendarManagement.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CalendarService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<List<String>> getCalendar(Long employeeId) {
        List<List<String>> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.findByEmployeeId(employeeId);
        if (meetings.size() == 0) {
            throw new IllegalArgumentException("No meetings");
        }
        for (Meeting meeting : meetings) {
            List<String> temp = new ArrayList<>();
            temp.add("Start time " + String.valueOf(meeting.getStartTime()));
            temp.add("end time " + String.valueOf(meeting.getEndTime()));
            temp.add("title " + String.valueOf(meeting.getMeetingTitle()));
            result.add(temp);
        }
        return result;
    }

    public ResponseEntity<String> bookMeeting(Long employeeId, LocalTime startTime, LocalTime endTime, String title, List<String> attendeeEmails) {
        List<Employee> attendees = employeeRepository.findByEmailIn(attendeeEmails);
        List<Meeting> meetings = meetingRepository.findByEmployeeId(employeeId);

        if (attendees.isEmpty()) {
            throw new IllegalArgumentException("No valid employees found");
        }

        for (Meeting meeting : meetings) {
            // Check for any overlap
            if (!(meeting.getEndTime().isBefore(startTime) || meeting.getStartTime().isAfter(endTime))) {
                throw new MeetingConflictException("Meeting overlap between " + meeting.getStartTime() + " and " + meeting.getEndTime());
            }
        }

        meetingRepository.save(new Meeting(title, startTime, endTime, employeeId));
        for (Employee employee : attendees) {
            if(employee.getEmployeeId() == employeeId){
                throw new CannotBookMeetingWithYourselfException(employeeId + " Cannot Book a Meeting with themself " );
            }
            meetingRepository.save(new Meeting(title, startTime, endTime, employee.getEmployeeId()));
        }
        return ResponseEntity.ok("Meeting added");
    }


    public List<String> getAvailableSlots(Long employeeId1, Long employeeId2) {

        //for storing the available time slots
        List<String> availableTimeSlots = new ArrayList<>();

        //getting the meetings of the two employees
        List<Meeting> meetings1 = meetingRepository.findByEmployeeId(employeeId1);
        List<Meeting> meetings2 = meetingRepository.findByEmployeeId(employeeId2);

        //setting the working hours
        LocalTime workingStart = LocalTime.of(8, 0);
        LocalTime workingEnd = LocalTime.of(17, 0);

        // Define 1-hour slots
        List<LocalTime> slots = new ArrayList<>();
        for (LocalTime time = workingStart; time.isBefore(workingEnd); time = time.plusHours(1)) {
            slots.add(time);
        }

        //marking the unavailable slots
        boolean[] availableSlots = new boolean[9];
        Arrays.fill(availableSlots, true);

        // Mark busy slots for Employee 1
        markBusySlots(meetings1, availableSlots, slots);

        // Mark busy slots for Employee 2
        markBusySlots(meetings2, availableSlots, slots);

        for (int i = 0; i < slots.size(); i++) {
            if (availableSlots[i]) {
                LocalTime start = slots.get(i);
                LocalTime end = start.plusHours(1);
                availableTimeSlots.add("A slot between:" + start + "to" + end + "is Available");
            }
        }
        return availableTimeSlots;
    }

    private void markBusySlots(List<Meeting> meetings, boolean[] availableSlots, List<LocalTime> slots) {
        for (Meeting meeting : meetings) {
            LocalTime meetingStart = meeting.getStartTime();
            LocalTime meetingEnd = meeting.getEndTime();

            // Iterate over all defined 1-hour slots
            for (int i = 0; i < slots.size(); i++) {
                LocalTime slotStart = slots.get(i);
                LocalTime slotEnd = slotStart.plusHours(1);

                // If the meeting overlaps with the current slot, mark it as unavailable
                if (!(meetingEnd.isBefore(slotStart) || meetingStart.isAfter(slotEnd))) {
                    availableSlots[i] = false;
                }
            }
        }

    }
}
