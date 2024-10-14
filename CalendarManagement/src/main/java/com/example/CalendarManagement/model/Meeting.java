package com.example.CalendarManagement.model;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.CalendarManagement.repository.MeetingRepository;
import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MeetingId;
    private Long employeeId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String MeetingTitle;

//    @ManyToMany
//    @JoinTable(
//            name = "employee_meeting", // Name of the join table
//            joinColumns = @JoinColumn(name = "employee_id"),
//            inverseJoinColumns = @JoinColumn(name = "meeting_id")
//    )
//    private List<Employee> attendees = new ArrayList<>();



    public java.lang.Long getMeetingId() {
        return MeetingId;
    }

    public Long getEmployeeId(){
        return this.employeeId;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public LocalTime getEndTime(){
        return this.endTime;
    }

    public String getMeetingTitle(){
        return this.MeetingTitle;
    }

    public Meeting(String title, LocalTime startTime, LocalTime endTime, Long employeeId) {
        this.MeetingTitle = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
    }


}
