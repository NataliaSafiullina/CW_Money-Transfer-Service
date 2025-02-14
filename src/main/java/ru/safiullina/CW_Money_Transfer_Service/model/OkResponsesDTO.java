package ru.safiullina.CW_Money_Transfer_Service.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OkResponsesDTO that = (OkResponsesDTO) o;
        return Objects.equals(getOperationId(), that.getOperationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperationId());
    }
}
