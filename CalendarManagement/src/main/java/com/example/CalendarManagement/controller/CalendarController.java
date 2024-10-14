package com.example.CalendarManagement.controller;

import com.example.CalendarManagement.exception.CannotBookMeetingWithYourselfException;
import com.example.CalendarManagement.exception.MeetingConflictException;
import com.example.CalendarManagement.model.Employee;
import com.example.CalendarManagement.model.Meeting;
import com.example.CalendarManagement.service.CalendarService;
import com.example.CalendarManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/start")
    public String Hello(){
        return "Hello world";
    }

    @PostMapping("/newEmployeeRegistration")
    public ResponseEntity<String> addEmployee(@RequestParam String name, @RequestParam String email){
        if(employeeService.addEmployee(name, email)){
            return ResponseEntity.ok("Employee Added");
        }
        return ResponseEntity.badRequest().body("duplicate value");
    }
//
    @PostMapping("/{employeeId}/meeting")
    public ResponseEntity<String> bookMeeting(@PathVariable Long employeeId, @RequestParam LocalTime startTime, @RequestParam LocalTime endTime, @RequestParam String MeetingTitle, @RequestParam List<String> attendeeEmails){
        calendarService.bookMeeting(employeeId, startTime, endTime, MeetingTitle, attendeeEmails);
        return ResponseEntity.ok("Meeting booked");
    }

    @GetMapping("/{employeeId}/MyMeetings")
    public ResponseEntity<List<List<String>>> getMySchedule(@PathVariable Long employeeId){
        List<List<String>> result = calendarService.getCalendar(employeeId);
        System.out.print(result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/findASlot")
    public ResponseEntity<List<String>> getTimeSlots(@RequestParam Long EmployeeId1, @RequestParam Long EmployeeId2){
        List<String> timeSlots = calendarService.getAvailableSlots(EmployeeId1, EmployeeId2);
        return ResponseEntity.ok(timeSlots);
    }

    @ExceptionHandler(MeetingConflictException.class)
    public ResponseEntity<String> handleMeetingConflictException(MeetingConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    public ResponseEntity<String> handleSelfBookingException(CannotBookMeetingWithYourselfException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
