package com.mycompany.ExpirationManagerApi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * Класс для передачи данных о статусе банковских карт между клиентом и сервером.
 * Явяляется оберткой над перечислимым типом CardStatus.
 */
@Builder
@Getter
public class CardStatusDto {
    private CardStatus status;

    /**
     * Создает экземпляр класса, соответствующий статусу карты OK
     * @return экземпляр класса, соответствующий данному статусу
     */
    public static CardStatusDto ok() {
        return CardStatusDto.builder().status(CardStatus.OK).build();
    }

    /**
     *  Создает экземпляр класса, соответствующий статусу карты ANNULLED (аннулирована)
     * @return экземпляр класса, соответствующий данному статусу
     */
    public static CardStatusDto annulled() {
        return CardStatusDto.builder().status(CardStatus.ANNULLED).build();
    }

    /**
     * Создает экземпляр класса, соответствующий статусу карты EXPIRED (истекла)
     * @return экземпляр класса, соответствующий данному статусу
     */
    public static CardStatusDto expired() {
        return CardStatusDto.builder().status(CardStatus.EXPIRED).build();
    }

    /**
     * Создает экземпляр класса, соответствующий заданному статусу карты
     * @param status CardStatus,
     */
    @JsonCreator
    public CardStatusDto(@JsonProperty(value = "status") CardStatus status) {
        this.status = status;
    }
}
