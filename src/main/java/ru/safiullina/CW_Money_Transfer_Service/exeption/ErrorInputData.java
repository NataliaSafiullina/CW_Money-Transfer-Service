package ru.safiullina.CW_Money_Transfer_Service.exeption;

public class ErrorInputData extends RuntimeException{
    public ErrorInputData(String message) {
        super(message);
    }
}
