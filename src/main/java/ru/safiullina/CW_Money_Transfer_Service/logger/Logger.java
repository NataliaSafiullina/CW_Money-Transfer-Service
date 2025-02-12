package ru.safiullina.CW_Money_Transfer_Service.logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Logger {

    void log(String msg);

    static Logger getInstance() {
        return null;
    }

    BufferedWriter createLogFile(String fileName) throws IOException;
}