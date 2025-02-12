package ru.safiullina.CW_Money_Transfer_Service.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerImpl implements Logger {
    private static LoggerImpl logger;

    // Дата и формат даты
    private final LocalDateTime currentDate = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Метод вывода лога в файл по умолчанию
     * @param msg - сообщение, которое вывести
     */
    @Override
    public void log(String msg) {
        String fileName = "log_" + System.getProperty("user.name") + ".log";
        try (BufferedWriter bw = createLogFile(fileName)) {
            // Формируем строку
            String formattedDate = formatter.format(currentDate);
            msg = "<" + formattedDate + "> " + msg + "\r\n";
            // Запишем строку
            bw.write(msg);
            bw.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * LoggerImpl - singleton, вместо конструктора вызываем этот метод,
     * получаем один и тот же экземпляр класса, если он уже создан,
     * и создаем, если он ещё не создан.
     * @return объект класса LoggerImpl
     */

    public static Logger getInstance() {
        if (logger == null) logger = new LoggerImpl();
        return logger;
    }

    /**
     * Запрещаем пользователям пользоваться
     * конструктором нашего класса, делая его приватным
     */
    private LoggerImpl() {
    }

    /**
     * Создает или открывает файл лога
     * (сделано отдельным методом, чтобы перехватить имя файла, которое передаётся.)
     * @return возвращает объект класса BufferedWriter
     */
    @Override
    public BufferedWriter createLogFile(String fileName) throws IOException {
        return new BufferedWriter(new FileWriter(fileName, true));
    }

}