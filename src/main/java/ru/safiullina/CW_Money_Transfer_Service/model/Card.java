package ru.safiullina.CW_Money_Transfer_Service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.Objects;

/**
 *  Класс Карты - банковские карты, имеют три обязательных атрибута:
 *   "cardNumber": "string" - номер карты из 16 символов,
 *   "cardValidTill": "string" - срок карты, месяц и год MM/YY,
 *   "cardCVV": "string" - код карты, 3 символа,
 *  Атрибут cardSums - пара
 */
public class Card {
    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank
    @Size(min = 5, max = 5)
    private String cardValidTill;

    @NotBlank
    @Size(min = 3, max = 3)
    private String cardCVV;

    private Map<String, Long> cardSums;

    // TODO: забыла зачем нам пустой конструктор
    public Card() {}
    public Card(String cardNumber, String cardValidTill, String cardCVV) {
        this.cardNumber = cardNumber;
        this.cardValidTill = cardValidTill;
        this.cardCVV = cardCVV;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardValidTill() {
        return cardValidTill;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public Map<String, Long> getCardSums() {
        return cardSums;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardValidTill(String cardValidTill) {
        this.cardValidTill = cardValidTill;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public void setCardSums(Map<String, Long> cardSums) {
        this.cardSums = cardSums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(getCardNumber(), card.getCardNumber()) && Objects.equals(getCardValidTill(), card.getCardValidTill()) && Objects.equals(getCardCVV(), card.getCardCVV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumber(), getCardValidTill(), getCardCVV());
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardValidTill='" + cardValidTill + '\'' +
                ", cardCVV='" + cardCVV + '\'' +
                ", cardSums=" + cardSums +
                '}';
    }
}
