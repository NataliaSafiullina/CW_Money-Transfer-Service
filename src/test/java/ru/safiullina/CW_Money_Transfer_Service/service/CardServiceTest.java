package ru.safiullina.CW_Money_Transfer_Service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.safiullina.CW_Money_Transfer_Service.model.Card;
import ru.safiullina.CW_Money_Transfer_Service.model.Confirmation;
import ru.safiullina.CW_Money_Transfer_Service.model.OkResponsesDTO;
import ru.safiullina.CW_Money_Transfer_Service.model.Transaction;
import ru.safiullina.CW_Money_Transfer_Service.repository.CardRepository;
import ru.safiullina.CW_Money_Transfer_Service.repository.CardRepositoryImpl;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

class CardServiceTest {

    private static Confirmation confirmationOk;

    private static Transaction transaction;
    private static Card card1;
    private static Card card2;

    @BeforeEach
    void createObjects() {
        confirmationOk = new Confirmation("1", "999");

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

    // TODO tests
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

        assertThat(correctResult.getOperationId(), is(okResponsesDTO.getOperationId()));
    }

    @Test
    void testTransfer() {
    }

    @Test
    void addCards() {
    }
}