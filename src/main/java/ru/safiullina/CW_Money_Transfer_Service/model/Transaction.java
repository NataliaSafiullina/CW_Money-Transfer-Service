package ru.safiullina.CW_Money_Transfer_Service.model;

/**
 * Объекты класса Transaction хранят данные о транзакциях.
 * Аналитики транзакций:
 * - отправитель
 * - получатель
 * - валюта
 * - сумма
 * - код подтверждения данной транзакции
 */
public class Transaction {
    private String cardFrom;
    private String cardTo;
    private String currency;
    private long value;
    private String code;

    public Transaction() {
    }

    public Transaction(String cardFrom, String cardTo, String currency, long value, String code) {
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.currency = currency;
        this.value = value;
        this.code = code;
    }

    public String getCardFrom() {
        return cardFrom;
    }

    public String getCardTo() {
        return cardTo;
    }

    public String getCurrency() {
        return currency;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
