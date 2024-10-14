package com.example.CalendarManagement.repository;

import com.example.CalendarManagement.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByEmployeeId(Long employeeId);
}
