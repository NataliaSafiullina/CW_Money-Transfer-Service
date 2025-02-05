package ru.safiullina.CW_Money_Transfer_Service.exeption;

public class ErrorConfirmation extends RuntimeException{
    public ErrorConfirmation(String message) {
        super(message);
    }
}
