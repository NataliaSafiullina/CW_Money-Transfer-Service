package ru.safiullina.CW_Money_Transfer_Service.exeption;

public class ErrorTransfer extends RuntimeException{
    public ErrorTransfer(String message) {
        super(message);
    }
}
