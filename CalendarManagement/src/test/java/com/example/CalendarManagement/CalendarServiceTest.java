package com.example.CalendarManagement;

import com.example.CalendarManagement.model.Meeting;
import com.example.CalendarManagement.repository.EmployeeRepository;
import com.example.CalendarManagement.repository.MeetingRepository;
import com.example.CalendarManagement.service.CalendarService;
import com.example.CalendarManagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.CalendarManagement.model.Employee;
import org.springframework.http.ResponseEntity;
import com.example.CalendarManagement.exception.MeetingConflictException;
import com.example.CalendarManagement.exception.CannotBookMeetingWithYourselfException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalendarServiceTest {

    @InjectMocks
    private CalendarService calendarService;

    @Mock
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCalendarSuccess() {
        Long employeeId = 1L;
        List<Meeting> meetings = Arrays.asList(
                new Meeting("Meeting 1", LocalTime.of(9, 0), LocalTime.of(10, 0), employeeId),
                new Meeting("Meeting 2", LocalTime.of(11, 0), LocalTime.of(12, 0), employeeId)
        );
        when(meetingRepository.findByEmployeeId(employeeId)).thenReturn(meetings);

        List<List<String>> result = calendarService.getCalendar(employeeId);
        assertEquals(2, result.size());
        assertEquals("Start time 09:00", result.get(0).get(0));
        assertEquals("title Meeting 1", result.get(0).get(2));
    }

    @Test
    void testGetCalendarNoMeetings() {
        Long employeeId = 1L;
        when(meetingRepository.findByEmployeeId(employeeId)).thenReturn(Arrays.asList());

        assertThrows(IllegalArgumentException.class, () -> {
            calendarService.getCalendar(employeeId);
        });
    }

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    // Open Mockito mocks
    public void EmployeeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Test to check if duplicate email throws an exception
    @Test
    void testAddEmployeeDuplicateEmail() {
        String email = "test@example.com";

        // Mocking the repository to return an existing employee when searching by email
        when(employeeRepository.findByEmail(email)).thenReturn(new Employee("Test User", email));

        // Expecting IllegalArgumentException to be thrown when trying to add a duplicate email
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee("Test User", email);
        });

        // Check if the thrown exception has the correct message
        String expectedMessage = "Employee with this email already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testBookMeetingSuccess() {
        Long employeeId = 1L;
        String title = "Team Sync";
        List<String> attendees = Arrays.asList("attendee1@example.com");

        // Since the Employee class constructor takes (name, email), the EmployeeId is managed by JPA
        when(employeeRepository.findByEmailIn(attendees)).thenReturn(Arrays.asList(new Employee("Attendee 1", "attendee1@example.com")));
        when(meetingRepository.findByEmployeeId(employeeId)).thenReturn(Arrays.asList());

        // Simulate successful booking of a meeting
        ResponseEntity<String> response = calendarService.bookMeeting(employeeId, LocalTime.of(10, 0), LocalTime.of(11, 0), title, attendees);

        // Check that the response body is correct
        assertEquals("Meeting added", response.getBody());

        // Verify that the meeting is saved twice (once for the organizer and once for the attendee)
        verify(meetingRepository, times(2)).save(any(Meeting.class));
    }


    @Test
    void testBookMeetingConflict() {
        Long employeeId = 1L;
        String attendeeEmail = "attendee@example.com";

        // Mocking the employee repository to return an employee for the given email
        when(employeeRepository.findByEmailIn(Arrays.asList(attendeeEmail)))
                .thenReturn(Arrays.asList(new Employee("Attendee", attendeeEmail)));

        // Mocking the meeting repository to return an existing meeting with overlapping times
        when(meetingRepository.findByEmployeeId(employeeId))
                .thenReturn(Arrays.asList(new Meeting("Existing Meeting", LocalTime.of(9, 00), LocalTime.of(10, 00), employeeId)));

        // Expecting a MeetingConflictException to be thrown due to the time overlap
        assertThrows(MeetingConflictException.class, () -> {
            calendarService.bookMeeting(employeeId, LocalTime.of(9, 0), LocalTime.of(11, 0), "New Meeting", Arrays.asList(attendeeEmail));
        });
    }


    @Test
    void testBookMeetingWithSelf() {
        Long employeeId = 1L;
        String email = "organizer@example.com";

        // Create a mock Employee object
        Employee mockEmployee = mock(Employee.class);
        when(mockEmployee.getEmployeeId()).thenReturn(employeeId);
        when(mockEmployee.getEmail()).thenReturn(email);

        // Mock the repository call to return the mocked Employee
        when(employeeRepository.findByEmailIn(Arrays.asList(email))).thenReturn(Arrays.asList(mockEmployee));

        // Expect that booking a meeting with oneself throws CannotBookMeetingWithYourselfException
        assertThrows(CannotBookMeetingWithYourselfException.class, () -> {
            calendarService.bookMeeting(employeeId, LocalTime.of(10, 0), LocalTime.of(11, 0), "Self Meeting", Arrays.asList(email));
        });
    }


}
