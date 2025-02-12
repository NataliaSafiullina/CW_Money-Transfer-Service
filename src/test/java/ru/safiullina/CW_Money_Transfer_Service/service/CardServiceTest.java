package ru.safiullina.CW_Money_Transfer_Service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.safiullina.CW_Money_Transfer_Service.exeption.ErrorInputData;
import ru.safiullina.CW_Money_Transfer_Service.model.*;
import ru.safiullina.CW_Money_Transfer_Service.repository.CardRepository;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

class CardServiceTest {

    private static Confirmation confirmationOk;
    private static Confirmation confirmationFail;

    private static Transaction transaction;
    private static Card card1;
    private static Card card2;

    @BeforeEach
    void createObjects() {
        confirmationOk = new Confirmation("1", "999");
        confirmationFail = new Confirmation("1", "777");

        card1 = new Card(
                "1234123412341234",
                "12/33",
                "111");
        card1.setCardSums(new HashMap<>() {{
            put("RUR", 100000L);
        }});

        card2 = new Card(
                "4321432143214321",
                "11/33",
                "222");
        card2.setCardSums(new HashMap<>() {{
            put("RUR", 200000L);
        }});

        transaction = new Transaction(card1,card2,"RUR",1000,"999");
    }

    @Test
    void testConfirm() {
        // Создаем заглушку
        CardRepository cardRepository = Mockito.mock(CardRepository.class);
        // когда мы вызываем метод getTransaction, подменить его и вернуть нашу транзакцию
        Mockito.when(cardRepository.getTransaction(confirmationOk.getOperationId()))
                .thenReturn(Optional.ofNullable(transaction));
        // когда мы вызываем метод удаления транзакции, подменить его и вернуть true (типа успешно удалили)
        Mockito.when(cardRepository.deleteTransaction(confirmationOk.getOperationId()))
                .thenReturn(true);

        CardService cardService = new CardService(cardRepository);

        OkResponsesDTO okResponsesDTO =  cardService.confirm(confirmationOk);
        OkResponsesDTO correctResult = new OkResponsesDTO("1");

        // Позитивный тест
        assertThat(correctResult.getOperationId(), is(okResponsesDTO.getOperationId()));
    }

    @Test
    void testConfirmFail() {
        // Создаем заглушку
        CardRepository cardRepository = Mockito.mock(CardRepository.class);
        // когда мы вызываем метод getTransaction, подменить его и вернуть нашу транзакцию
        Mockito.when(cardRepository.getTransaction(confirmationFail.getOperationId()))
                .thenReturn(Optional.ofNullable(transaction));
        // когда мы вызываем метод удаления транзакции, подменить его и вернуть true (типа успешно удалили)
        Mockito.when(cardRepository.deleteTransaction(confirmationFail.getOperationId()))
                .thenReturn(true);

        CardService cardService = new CardService(cardRepository);

        // Сверим класс ошибки
        assertThrows(ErrorInputData.class, () -> {cardService.confirm(confirmationFail);});
    }

    @Test
    void testTransferOk() {
        Amount amount = new Amount(1000, "RUR");
        Transfer transfer = new Transfer(
                card1.getCardNumber(),
                card1.getCardValidTill(),
                card1.getCardCVV(),
                card2.getCardNumber(),
                amount);

        // Создаем заглушку
        CardRepository cardRepository = Mockito.mock(CardRepository.class);
        // когда мы вызываем метод addTransaction, подменить его и вернуть true
        Mockito.when(cardRepository.addTransaction("1", transaction))
                .thenReturn(true);
        Mockito.when(cardRepository.getByNumber(card1.getCardNumber())).thenReturn(Optional.ofNullable(card1));
        Mockito.when(cardRepository.getByNumber(card2.getCardNumber())).thenReturn(Optional.ofNullable(card2));

        CardService cardService = new CardService(cardRepository);

        OkResponsesDTO okResponsesDTO =  cardService.transfer(transfer);
        OkResponsesDTO correctResult = new OkResponsesDTO("1");

        // Позитивный тест
        assertThat(correctResult.getOperationId(), is(okResponsesDTO.getOperationId()));

    }    @Test
    void testTransferFail() {
        Amount amount = new Amount(1000000000000L, "RUR");
        Transfer transfer = new Transfer(
                card1.getCardNumber(),
                card1.getCardValidTill(),
                card1.getCardCVV(),
                card2.getCardNumber(),
                amount);

        // Создаем заглушку
        CardRepository cardRepository = Mockito.mock(CardRepository.class);
        // когда мы вызываем метод addTransaction, подменить его и вернуть true
        Mockito.when(cardRepository.addTransaction("1", transaction))
                .thenReturn(true);
        // Подставим и получим наши тестовые карты
        Mockito.when(cardRepository.getByNumber(card1.getCardNumber())).thenReturn(Optional.ofNullable(card1));
        Mockito.when(cardRepository.getByNumber(card2.getCardNumber())).thenReturn(Optional.ofNullable(card2));

        CardService cardService = new CardService(cardRepository);

        // Сверим текст ошибки
        assertThrows(ErrorInputData.class, () -> {cardService.transfer(transfer);}, "Error input data: not enough money");

    }

}