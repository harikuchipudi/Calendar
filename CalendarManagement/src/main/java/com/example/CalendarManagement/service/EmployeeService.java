package com.example.CalendarManagement.service;

import com.example.CalendarManagement.model.Employee;
import com.example.CalendarManagement.model.Meeting;
import com.example.CalendarManagement.repository.EmployeeRepository;
import com.example.CalendarManagement.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public boolean addEmployee(String name, String email){
        if (employeeRepository.findByEmail(email) != null )
            throw new IllegalArgumentException("Employee with this email already exists");
        else{
            employeeRepository.save(new Employee(name, email));
            return true;
        }
    }


}
