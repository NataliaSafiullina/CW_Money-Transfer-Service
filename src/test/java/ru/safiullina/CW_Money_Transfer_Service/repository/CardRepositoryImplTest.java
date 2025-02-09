package ru.safiullina.CW_Money_Transfer_Service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.safiullina.CW_Money_Transfer_Service.model.Card;
import ru.safiullina.CW_Money_Transfer_Service.model.Transaction;

import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CardRepositoryImplTest {

    private final CardRepository repository = new CardRepositoryImpl();
    private static Transaction transaction;
    private static Card card1;
    private static Card card2;


    @BeforeEach
    void createObjects(){
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
    void testAddCard() {
        assertThat(card1, is(repository.addCard(card1)));
    }
    @Test
    void testAddCardFail() {
        assertThat(card1, not(repository.addCard(card2)));
    }


    @Test
    void testGetByNumber() {
        repository.addCard(card1);
        assertThat(Optional.of(card1), is(repository.getByNumber(card1.getCardNumber())));
    }

    @Test
    void addTransactionTrue() {
       assertThat(true, is(repository.addTransaction("1",transaction)));
    }
    @Test
    void addTransactionFalse() {
        assertThat(false, is(repository.addTransaction("1",null)));
    }

    @Test
    void getTransaction() {
        repository.addTransaction("1",transaction);
        assertThat(Optional.of(transaction), is(repository.getTransaction("1")));
    }

    @Test
    void deleteTransaction() {
        repository.addTransaction("1",transaction);
        repository.deleteTransaction("1");
        assertThat(Optional.empty(), is(repository.getTransaction("1")));
    }
}