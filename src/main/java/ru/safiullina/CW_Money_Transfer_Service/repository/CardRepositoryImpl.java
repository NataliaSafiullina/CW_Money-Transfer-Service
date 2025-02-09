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

    // transactions - хранит не подтвержденные транзакции, где String - это id операции и ключ мапы
    private final ConcurrentHashMap<String, Transaction> transactions = new ConcurrentHashMap<>();


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


    /**
     * addTransaction - сохраняет в хранилище неподтвержденную операцию.
     * Операция будет лежать там, пока её не подтвердит пользователь.
     * @param operationId - id операции
     * @param transaction - транзакция
     * @return - вернем true при успешной записи, и false при ошибках записи
     */
    @Override
    public boolean addTransaction(String operationId, Transaction transaction) {
        try{
            transactions.put(operationId, transaction);
        }
        catch (NullPointerException e){
            return false;
        }
        return transactions.containsKey(operationId);
    }

    /**
     * getTransaction - метод получения транзакции по её id.
     * @param operationId - id операции
     * @return - вернем транзакцию, если она существует, иначе будет empty()
     */
    @Override
    public Optional<Transaction> getTransaction(String operationId) {
        return Optional.ofNullable(transactions.get(operationId));
    }

    /**
     * deleteTransaction - при успешно проведенной операции, мы должны удалить транзакцию из списка не проведенных.
     * @param operationId - id операции
     * @return - вернем true при успешном удалении, false при ошибках
     */
    @Override
    public boolean deleteTransaction(String operationId) {
        try{
            transactions.remove(operationId);
        }
        catch (NullPointerException e){
            return false;
        }
        return true;
    }
}
