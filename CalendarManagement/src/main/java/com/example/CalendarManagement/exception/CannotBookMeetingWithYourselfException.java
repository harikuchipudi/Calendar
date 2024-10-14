package com.example.CalendarManagement.exception;

public class CannotBookMeetingWithYourselfException extends RuntimeException{
    public CannotBookMeetingWithYourselfException(String message){
        super(message);
    }
}
