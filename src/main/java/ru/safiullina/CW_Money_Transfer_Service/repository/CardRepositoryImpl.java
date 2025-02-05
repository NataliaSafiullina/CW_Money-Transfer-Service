package ru.safiullina.CW_Money_Transfer_Service.repository;

import org.springframework.stereotype.Repository;
import ru.safiullina.CW_Money_Transfer_Service.model.Card;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepositoryImpl implements CardRepository {

    // cards - храним список карт, искать будем по номеру карты, который будет ключом
    protected ConcurrentHashMap<String, Card> cards = new ConcurrentHashMap<>();


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
}
