package com.driver;

public class AlreadyDisconnectedException extends Exception{
    public AlreadyDisconnectedException(String message){
        super(message);
    }
}
