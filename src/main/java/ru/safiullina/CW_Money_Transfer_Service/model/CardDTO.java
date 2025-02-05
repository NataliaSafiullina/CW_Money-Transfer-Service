package ru.safiullina.CW_Money_Transfer_Service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CardDTO {
    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotBlank
    @Size(min = 5, max = 5)
    private String cardValidTill;

    @NotBlank
    @Size(min = 3, max = 3)
    private String cardCVV;

    public CardDTO() {
    }

    public CardDTO(String cardNumber, String cardValidTill, String cardCVV) {
        this.cardNumber = cardNumber;
        this.cardValidTill = cardValidTill;
        this.cardCVV = cardCVV;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardValidTill() {
        return cardValidTill;
    }

    public void setCardValidTill(String cardValidTill) {
        this.cardValidTill = cardValidTill;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }
}
