package com.example.CalendarManagement;

import com.example.CalendarManagement.service.CalendarService;
import com.example.CalendarManagement.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalendarManagementApplicationTests {

	@Autowired
	private CalendarService calendarService;

	@Autowired
	private EmployeeService employeeService;

	@Test
	void contextLoads() {
	}

	@Test
	void testCalendarServiceBeanExists() {
		// This test checks if the CalendarService bean is loaded into the application context
		assertThat(calendarService).isNotNull();
	}

	@Test
	void testEmployeeServiceBeanExists(){
		// This test checks if the EmployeeService bean is loaded into the application context
		assertThat(employeeService).isNotNull();
	}
}
