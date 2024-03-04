package com.mycompany.ExpirationManagerApi.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardStatus {
    @JsonProperty("OK") OK,

    @JsonProperty("EXPIRED") EXPIRED,

    @JsonProperty("ANNULLED") ANNULLED;
}
