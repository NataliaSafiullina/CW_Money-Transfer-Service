package ru.safiullina.CW_Money_Transfer_Service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.safiullina.CW_Money_Transfer_Service.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CwMoneyTransferServiceApplicationTests {

    private static Card card1;
    private static Card card2;

    private static Amount amount;
    private static Transfer transfer;

    private static Confirmation confirmationOk;
    private static Confirmation confirmationFailId;
    private static Confirmation confirmationFailCode;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> app5500 = new GenericContainer<>("transferapp:1.0")
            .withExposedPorts(5500);

    @BeforeAll
    static void makeObjects() {
        card1 = new Card(
                "1234123412341234",
                "12/33",
                "111");

        card2 = new Card(
                "4321432143214321",
                "11/33",
                "222");

        amount = new Amount(1000, "RUR");

        transfer = new Transfer(
                card1.getCardNumber(),
                card1.getCardValidTill(),
                card1.getCardCVV(),
                card2.getCardNumber(),
                amount);

        confirmationOk = new Confirmation("1", "999");
        confirmationFailId = new Confirmation("111", "999");
        confirmationFailCode = new Confirmation("1", "777");

    }

    @Test
    void transferOkTest() {
        // дано
        amount.setValue(100);
        amount.setCurrency("RUR");
        // когда
        ResponseEntity<OkResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/transfer", transfer, OkResponsesDTO.class);
        // ожидаем
        OkResponsesDTO correctResult = new OkResponsesDTO(testResult.getBody().getOperationId());
        // сравнение
        assertThat(correctResult, is(testResult.getBody()));
    }

    @Test
    void transferErrorInputDataTest() {
        // портим входные данные, ставим очень большую сумму
        amount.setValue(100000000000000L);
        // ожидаем
        ErrorResponsesDTO correctResult = new ErrorResponsesDTO("Error input data: not enough money", 0);
        // когда
        ResponseEntity<ErrorResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/transfer", transfer, ErrorResponsesDTO.class);
        // сравним
        assertThat(correctResult, is(testResult.getBody()));
    }

    @Test
    void transferErrorCodeTest() {
        // портим входные данные, ставим валюту, которой нет в наших картах
        amount.setCurrency("USD");
        ResponseEntity<ErrorResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/transfer", transfer, ErrorResponsesDTO.class);
        assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(testResult.getStatusCode()));
    }


    @Test
    void confirmOkTest() {
        // создадим транзакцию, номер операции будет равно 1
        ResponseEntity<OkResponsesDTO> postResponse = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/transfer", transfer, OkResponsesDTO.class);
        // при положительном ответе на confirmation будет номер операции 2
        OkResponsesDTO correctResult = new OkResponsesDTO(String.valueOf(
                Integer.parseInt(postResponse.getBody().getOperationId()) + 1));
        // когда
        ResponseEntity<OkResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/confirmOperation", confirmationOk, OkResponsesDTO.class);
        // сравнение
        assertThat(correctResult, is(testResult.getBody()));
    }

    @Test
    void confirmErrorId() {
        // когда
        ResponseEntity<OkResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/confirmOperation", confirmationFailId, OkResponsesDTO.class);
        // сравнение
        assertThat(HttpStatus.BAD_REQUEST, is(testResult.getStatusCode()));
    }

    @Test
    void confirmErrorCodeTest() {
        // создадим транзакцию, номер операции будет равно 1
        ResponseEntity<OkResponsesDTO> postResponse = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/transfer", transfer, OkResponsesDTO.class);
        // ожидаем
        ErrorResponsesDTO correctResult = new ErrorResponsesDTO("Error input data: invalid code", 0);
        // когда
        ResponseEntity<ErrorResponsesDTO> testResult = restTemplate.postForEntity("http://localhost:" +
                app5500.getMappedPort(5500) + "/confirmOperation", confirmationFailCode, ErrorResponsesDTO.class);
        // сравнение
        assertThat(correctResult, is(testResult.getBody()));
    }

}
