package ru.safiullina.CW_Money_Transfer_Service.repository;

import ru.safiullina.CW_Money_Transfer_Service.model.Card;
import ru.safiullina.CW_Money_Transfer_Service.model.Transaction;

import java.util.Optional;

public interface CardRepository {

    Card addCard(Card card);
    Optional<Card> getByNumber(String cardNumber);

    boolean addTransaction(long operationId, Transaction transaction);
    boolean deleteTransaction(long operationId);

}
