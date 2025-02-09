package ru.safiullina.CW_Money_Transfer_Service.model;

import java.util.Objects;

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
    private Card cardFrom;
    private Card cardTo;
    private String currency;
    private long value;
    private String code;

    public Transaction() {
    }

    public Transaction(Card cardFrom, Card cardTo, String currency, long value, String code) {
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.currency = currency;
        this.value = value;
        this.code = code;
    }

    public Card getCardFrom() {
        return cardFrom;
    }

    public Card getCardTo() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return getValue() == that.getValue() && Objects.equals(getCardFrom(), that.getCardFrom()) && Objects.equals(getCardTo(), that.getCardTo()) && Objects.equals(getCurrency(), that.getCurrency()) && Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardFrom(), getCardTo(), getCurrency(), getValue(), getCode());
    }
}
