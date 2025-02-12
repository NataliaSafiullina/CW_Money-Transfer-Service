package ru.safiullina.CW_Money_Transfer_Service.service;

import org.springframework.stereotype.Service;
import ru.safiullina.CW_Money_Transfer_Service.exeption.ErrorConfirmation;
import ru.safiullina.CW_Money_Transfer_Service.exeption.ErrorInputData;
import ru.safiullina.CW_Money_Transfer_Service.exeption.ErrorTransfer;
import ru.safiullina.CW_Money_Transfer_Service.logger.Logger;
import ru.safiullina.CW_Money_Transfer_Service.logger.LoggerImpl;
import ru.safiullina.CW_Money_Transfer_Service.model.*;
import ru.safiullina.CW_Money_Transfer_Service.repository.CardRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CardService {

    // operation ID - будем хранить идентификатор операции
    protected AtomicInteger operationId = new AtomicInteger();

    private final CardRepository cardRepository;

    private Logger logger = LoggerImpl.getInstance();

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public OkResponsesDTO confirm(Confirmation confirmation) {
        // Найдем транзакцию по коду операции
        Optional<Transaction> transaction = cardRepository.getTransaction(confirmation.getOperationId());

        if (transaction.isEmpty()) {
            throw new ErrorInputData("operation ID does not exist");
        }

        // Получим параметры транзакции
        Card cardFrom = transaction.get().getCardFrom();
        Card cardTo = transaction.get().getCardTo();
        String currency = transaction.get().getCurrency();
        long value = transaction.get().getValue();

        if (!Objects.equals(confirmation.getCode(), transaction.get().getCode())) {
            String error = "invalid code";
            logger.log(logTransaction(confirmation.getOperationId(), transaction.get(),
                    "confirmation: " + error, "fail"));
            throw new ErrorInputData(error);
        }

        // Сделаем списание и зачисление денег.
        long valueCardFrom = cardFrom.getCardSums().get(currency);
        long valueCardTo = cardTo.getCardSums().get(currency);
        if (!makeTransaction(cardFrom, cardTo, currency, value)) {
            String error = "operation failed";
            logger.log(logTransaction(confirmation.getOperationId(), transaction.get(),
                    "confirmation: " + error, "fail"));
            throw new ErrorConfirmation(error);
        }

        // Проверим результат перевода
        if ((cardTo.getCardSums().get(currency) - valueCardTo) != value
                || (valueCardFrom - cardFrom.getCardSums().get(currency)) != value) {
            String error = "operation failed";
            logger.log(logTransaction(confirmation.getOperationId(), transaction.get(),
                    "confirmation: " + error, "fail"));
            throw new ErrorConfirmation(error);
        }

        // Удалим транзакцию из списка неподтвержденных
        if (!cardRepository.deleteTransaction(confirmation.getOperationId())) {
            String error = "during deleting transaction";
            logger.log(logTransaction(confirmation.getOperationId(), transaction.get(),
                    "confirmation: " + error, "fail"));
            throw new ErrorConfirmation(error);
        }

        logger.log(logTransaction(confirmation.getOperationId(), transaction.get(),
                "confirmation", "OK"));
        return new OkResponsesDTO(Integer.toString(operationId.incrementAndGet()));
    }

    public OkResponsesDTO transfer(Transfer transfer) {
        // Создадим карты для тестовой работы, в реальной работе карты уже будут в хранилище
        addCards(transfer);

        // Получаем карты участвующие в операции
        List<Card> cards = getCards(transfer);
        Card cardFrom = cards.get(0);
        Card cardTo = cards.get(1);

        // Проводим валидацию карты отправителя
        if (!validateCardFrom(cardFrom, transfer)) {
            String error = "incorrect CVV or date";
            logger.log(logTransfer("", transfer, "transfer: " + error, "fail"));
            throw new ErrorInputData(error);
        }

        // Проверим параметры операции
        Amount amount = transfer.getAmount();
        if (amount == null) {
            String error = "incorrect value";
            logger.log(logTransfer("", transfer, "transfer: " + error, "fail"));
            throw new ErrorInputData(error);
        }
        // Получим и сохраним валюту и сумму перевода
        String currency = transfer.getAmount().getCurrency();
        long value = transfer.getAmount().getValue();

        // Проверим достаточно ли денег на карте отправителя
        if (cardFrom.getCardSums().get(currency) < value) {
            String error = "not enough money";
            logger.log(logTransfer("", transfer, "transfer: " + error, "fail"));
            throw new ErrorInputData(error);
        }

        // Создадим макет транзакции, не подтвержденный перевод
        Transaction transaction = new Transaction(cardFrom, cardTo, currency, value, "999");
        if (!cardRepository.addTransaction(String.valueOf(operationId.incrementAndGet()), transaction)) {
            String error = "can not make an operation";
            logger.log(logTransfer("", transfer, "transfer: " + error, "fail"));
            throw new ErrorTransfer(error);
        }

        logger.log(logTransfer(Integer.toString(operationId.get()), transfer, "transfer", "OK"));
        return new OkResponsesDTO(Integer.toString(operationId.get()));
    }

    private boolean makeTransaction(Card cardFrom, Card cardTo, String currency, long value) {
        if (cardFrom.getCardSums().containsKey(currency)) {
            long amountFrom = cardFrom.getCardSums().get(currency) - value;
            if (amountFrom >= 0) {
                // Старт транзакции
                Map<String, Long> cardFromSums = new HashMap<>();
                cardFromSums.put(currency, amountFrom);
                cardFrom.setCardSums(cardFromSums);
                Map<String, Long> cardToSums = new HashMap<>();
                cardToSums.put(currency, cardTo.getCardSums().get(currency) + value);
                cardTo.setCardSums(cardToSums);
                // Конец транзакции
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private String logTransaction (String id, Transaction transaction, String operation, String result){
        return " [" + id + "] " +
                transaction.getCardFrom().getCardNumber() + " -> " + transaction.getCardTo().getCardNumber() +
                " ( " + transaction.getValue() + " " + transaction.getCurrency() + " ) " +
                operation + " ==> " + result;
    }


    private String logTransfer (String id, Transfer transfer, String operation, String result){
        return " [" + id + "] " +
                transfer.getCardFromNumber() + " -> " + transfer.getCardToNumber() +
                " ( " + transfer.getAmount().getValue() + " " + transfer.getAmount().getCurrency() + " ) " +
                operation + " ==> " + result;
    }

    private boolean validateCardFrom(Card cardFrom, Transfer transfer) {
        Card cardFromForValidation = new Card(
                transfer.getCardFromNumber(),
                transfer.getCardFromValidTill(),
                transfer.getCardFromCVV());
        return cardFrom.equals(cardFromForValidation);
    }

    private List<Card> getCards(Transfer transfer) {
        Card cardFrom = cardRepository.getByNumber(transfer.getCardFromNumber()).orElseThrow(
                () -> new ErrorInputData("card " + transfer.getCardFromNumber() + " does not exist"));
        Card cardTo = cardRepository.getByNumber(transfer.getCardToNumber()).orElseThrow(
                () -> new ErrorInputData("card " + transfer.getCardToNumber() + " does not exist"));
        return List.of(cardFrom, cardTo);
    }

    /**
     * Метод addCards добавляет набор из двух карт в хранилище чтобы на них проверить работу сервиса.
     *
     * @param transfer - получаем от фронта введенные данные и создаем из них карты.
     */
    public void addCards(Transfer transfer) {
        Card card1 = new Card(
                transfer.getCardFromNumber(),
                transfer.getCardFromValidTill(),
                transfer.getCardFromCVV());
        card1.setCardSums(new HashMap<>() {{
            put("RUR", 100000L);
        }});
        cardRepository.addCard(card1);

        Card card2 = new Card(
                transfer.getCardToNumber(),
                transfer.getCardFromValidTill(),
                transfer.getCardFromCVV());
        card2.setCardSums(new HashMap<>() {{
            put("RUR", 200000L);
        }});
        cardRepository.addCard(card2);
    }

}
