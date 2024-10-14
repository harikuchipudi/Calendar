package com.example.CalendarManagement.repository;

import com.example.CalendarManagement.model.Employee;
import com.example.CalendarManagement.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);

    List<Employee> findByEmailIn(List<String> attendeeEmails);
//
//    List<Employee> findByEmailIn(List<String> attendeeEmails);


//    List<Meeting> getEmployeeById();
}
