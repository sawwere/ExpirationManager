package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardStatus {
    @JsonProperty("OK") OK,

    @JsonProperty("EXPIRED") EXPIRED,

    @JsonProperty("ANNULLED") ANNULLED;
}
