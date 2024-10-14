package com.example.CalendarManagement.exception;

public class MeetingConflictException extends RuntimeException {

    public MeetingConflictException(String message){
        super(message);
    }
}
