package ru.safiullina.CW_Money_Transfer_Service.model;

/**
 * Класс ErrorResponses, его задача - принять наше сообщение об ошибке
 * и переслать его фронту вместе со статусом.
 * У нас в приложении пока следующие ошибки:
 * 400 - Error input data
 * 500 - Error transfer,
 * 500 - Error confirmation.
 * Их схема:
 * {
 *   "message": "string",
 *   "id": 0
 * }
 * Источник статья: <a href="https://habr.com/ru/articles/675716/">ResponseEntity</a>
 */
public class ErrorResponsesDTO {
    private String message;
    private int id;

    public ErrorResponsesDTO() {}

    public ErrorResponsesDTO(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
