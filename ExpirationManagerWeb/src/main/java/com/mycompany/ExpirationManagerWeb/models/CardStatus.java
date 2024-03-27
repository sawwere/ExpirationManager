package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Перечислимый тип, отвечающий за возможные статусы банковских карт.
 */
public enum CardStatus {
    /**
     * Нормальное состояние банковской карты
     */
    @JsonProperty("OK") OK,
    /**
     * Банковская карта истекла
     */
    @JsonProperty("EXPIRED") EXPIRED,
    /**
     * Банковская карта была аннулирована
     */
    @JsonProperty("ANNULLED") ANNULLED;
}
