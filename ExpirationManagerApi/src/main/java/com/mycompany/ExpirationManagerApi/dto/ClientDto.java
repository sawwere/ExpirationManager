package com.mycompany.ExpirationManagerApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String passport;

    private LocalDate birthday;
}
