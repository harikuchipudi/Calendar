package com.example.CalendarManagement;

import com.example.CalendarManagement.model.Employee;
import com.example.CalendarManagement.repository.EmployeeRepository;
import com.example.CalendarManagement.repository.MeetingRepository;
import com.example.CalendarManagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

//    @Mock
//    private MeetingRepository meetingRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
    }

    // Test case 1: Successfully add a new employee
    @Test
    void testAddEmployeeSuccess() {
        String name = "John Doe";
        String email = "johndoe@example.com";

        // Mock the employee repository to return null (indicating no employee exists with the email)
        when(employeeRepository.findByEmail(email)).thenReturn(null);

        // Test the service method
        boolean result = employeeService.addEmployee(name, email);

        // Verify that the employee was saved
        verify(employeeRepository, times(1)).save(any(Employee.class));
        assertTrue(result);  // The method should return true upon success
    }

    // Test case 2: Add an employee with an existing email (should throw an exception)
    @Test
    void testAddEmployeeEmailExists() {
        String name = "John Doe";
        String email = "johndoe@example.com";

        // Mock the employee repository to return an employee (indicating the email already exists)
        when(employeeRepository.findByEmail(email)).thenReturn(new Employee(name, email));

        // Test that an exception is thrown when trying to add an employee with an existing email
        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(name, email);
        });

        // Verify that the employee was not saved
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
