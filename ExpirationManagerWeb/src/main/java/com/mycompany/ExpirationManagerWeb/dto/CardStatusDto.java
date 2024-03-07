package com.mycompany.ExpirationManagerWeb.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.ExpirationManagerWeb.models.CardStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardStatusDto {
    private CardStatus status;

    public static CardStatusDto ok() {
        return CardStatusDto.builder().status(CardStatus.OK).build();
    }

    public static CardStatusDto annulled() {
        return CardStatusDto.builder().status(CardStatus.ANNULLED).build();
    }

    public static CardStatusDto expired() {
        return CardStatusDto.builder().status(CardStatus.EXPIRED).build();
    }

//    @JsonCreator
//    public CardStatusDto(@JsonProperty(value = "status") String status) {
//        status = status.toUpperCase();
//        this.status = CardStatus.valueOf(status);
//    }

    @JsonCreator
    public CardStatusDto(@JsonProperty(value = "status") CardStatus status) {
        this.status = status;
    }
}
