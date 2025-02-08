package ru.safiullina.CW_Money_Transfer_Service.repository;

import org.springframework.stereotype.Repository;
import ru.safiullina.CW_Money_Transfer_Service.model.Card;
import ru.safiullina.CW_Money_Transfer_Service.model.Transaction;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepositoryImpl implements CardRepository {

    // cards - храним список карт, искать будем по номеру карты, который будет ключом
    private final ConcurrentHashMap<String, Card> cards = new ConcurrentHashMap<>();
    // transactions - хранит не подтвержденные транзакции
    private final ConcurrentHashMap<Long, Transaction> transactions = new ConcurrentHashMap<>();


    /**
     * addCard - сохранит карту в хранилище.
     * @param card - карта
     * @return - вернуть карта или null
     */
    @Override
    public Card addCard(Card card) {
        if (card == null) {
            return null;
        }
        cards.put(card.getCardNumber(), card);
        return card;

    }

    /**
     * getByNumber - поиск карты в хранилище по её номеру
     * @param cardNumber - номер карты
     * @return - вернуть карту или ничего, если такой нет
     */
    @Override
    public Optional<Card> getByNumber(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }

    // TODO добавление транзакции в хранилище
    @Override
    public boolean addTransaction(long operationId, Transaction transaction) {
        transactions.put(operationId, transaction);
        return false;
    }

    // TODO удаление транзакции их хранилища
    @Override
    public boolean deleteTransaction(long operationId) {
        return false;
    }
}
