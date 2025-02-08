package ru.safiullina.CW_Money_Transfer_Service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.safiullina.CW_Money_Transfer_Service.model.Confirmation;
import ru.safiullina.CW_Money_Transfer_Service.model.OkResponsesDTO;
import ru.safiullina.CW_Money_Transfer_Service.model.Transfer;
import ru.safiullina.CW_Money_Transfer_Service.service.CardService;

/**
 * Аннотация RestController включает две аннотации и Controller и ResponseBody,
 * т.е. говорит, что это контроллер и использует конвертер ответов
 */
@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/transfer")
    public OkResponsesDTO transfer(@RequestBody Transfer transfer){
        System.out.println(transfer);
        return cardService.transfer(transfer);
    }

    @PostMapping("/confirmOperation")
    public OkResponsesDTO confirm(@RequestBody Confirmation confirmation){
        System.out.println(confirmation);
        return cardService.confirm(confirmation);
    }
}
