package com.example.CalendarManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example.CalendarManagement.repository")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CalendarManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarManagementApplication.class, args);
	}

}
