package com.example.CalendarManagement.model;

import com.fasterxml.jackson.databind.node.LongNode;
import jakarta.persistence.*;



import javax.print.DocFlavor;
import java.util.List;
import java.util.ArrayList;



@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long EmployeeId;
    private String Name;

    @Column(unique = true)
    private String email;

    public String getEmail(){
        return this.email;
    }

    public Employee(String name, String email) {
        this.Name = name;
        this.email = email;
    }

    public Long getEmployeeId(){
        return this.EmployeeId;
    }
}
