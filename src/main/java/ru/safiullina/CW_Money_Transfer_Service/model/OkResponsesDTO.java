package ru.safiullina.CW_Money_Transfer_Service.model;

/**
 * Класс okResponses - его задача принять наше сообщение об успешной операции
 * и вернуть ответ (id операции) на фронт.
 */
public class OkResponsesDTO {
    private String operationId;

    public OkResponsesDTO() {
    }

    public OkResponsesDTO(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
