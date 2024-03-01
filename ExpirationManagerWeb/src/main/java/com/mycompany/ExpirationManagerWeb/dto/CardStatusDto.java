package com.mycompany.ExpirationManagerWeb.dto;

import com.mycompany.ExpirationManagerWeb.models.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class CardStatusDto {
    @NotNull
    private String status;

    public static CardStatusDto ok() {
        return CardStatusDto.builder().status(CardStatus.OK.toString()).build();
    }

    public static CardStatusDto annulled() {
        return CardStatusDto.builder().status(CardStatus.ANNULLED.toString()).build();
    }

    public static CardStatusDto expired() {
        return CardStatusDto.builder().status(CardStatus.EXPIRED.toString()).build();
    }

    public Boolean isValid() {
        String upperCased = status.toUpperCase();
        for (var cs : CardStatus.values())
            if (cs.toString().equals(upperCased)) {
                return true;
            }

        return false;
    }

    public CardStatusDto(String status) {
        CardStatus.valueOf(status.toUpperCase());
        this.status = status;
    }

    public CardStatus getCardStatus() {
        return CardStatus.valueOf(status.toUpperCase());
    }
}
