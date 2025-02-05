package ru.safiullina.CW_Money_Transfer_Service.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface cardMapper {

    Card mapCardDtoToCard (CardDTO cardDTO);
    CardDTO mapCardToCardDto(Card card);

}
