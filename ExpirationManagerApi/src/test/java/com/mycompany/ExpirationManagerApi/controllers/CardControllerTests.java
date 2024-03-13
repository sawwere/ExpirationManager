package com.mycompany.ExpirationManagerApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerApi.Application;
import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class})
@Transactional
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create_clients.sql", "/sql/create_cards.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/cards_after.sql", "/sql/clients_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
    }

    @Test
    void testGetAll() throws Exception{
        byte[] answer = this.mockMvc.perform(get(CardController.FIND_ALL_CARDS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto[] cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 5;
    }

    @Test
    void testDelete() throws Exception{
        this.mockMvc.perform(delete(CardController.DELETE_CARD, 0))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(delete(CardController.DELETE_CARD, 1))
                .andExpect(status().is2xxSuccessful());

        byte[] answer = this.mockMvc.perform(get(CardController.FIND_ALL_CARDS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto[] cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 4;
        assert Arrays.stream(cards).noneMatch(x -> x.getId() == 1);
    }

    @Test
    void testFindByClientAfterDelete() throws Exception{
        this.mockMvc.perform(delete(CardController.DELETE_CARD, 1))
                .andExpect(status().is2xxSuccessful());

        byte[] answer = this.mockMvc.perform(get(CardController.FIND_ALL_CARDS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto[] cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 4;
        assert Arrays.stream(cards).noneMatch(x -> x.getId() == 1);

        answer = this.mockMvc.perform(get(CardController.FIND_CARDS_BY_CLIENT, 2))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 2;
    }

    @Test
    void testFind() throws Exception {
        byte[] answer = this.mockMvc.perform(get(CardController.FIND_CARD, 1))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsByteArray();
        CardDto card = objectMapper.readValue(answer, CardDto.class);
        assert card.getId() == 1;
        assert card.equals(CardDto.builder()
                .id(1L)
                .cardNumber("9879671001709031")
                .dateOfExpiration(LocalDate.parse("2024-11-24"))
                .dateOfIssue(LocalDate.parse("2020-11-11"))
                .status(CardStatus.OK)
                .clientId(2L)
                .build());
    }

    @Test
    void testFindByClient() throws Exception {
        byte[] answer = this.mockMvc.perform(get(CardController.FIND_CARDS_BY_CLIENT, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto[] cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 2;

        answer = this.mockMvc.perform(get(CardController.FIND_CARDS_BY_CLIENT, 3))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        cards = objectMapper.readValue(answer, CardDto[].class);
        assert cards.length == 0;
    }

    @Test
    void testCreate() throws Exception {
        CardDto cardDto = CardDto.builder()
                .cardNumber("9879671001709036")
                .dateOfExpiration(LocalDate.parse("2024-11-24"))
                .dateOfIssue(LocalDate.parse("2020-11-11"))
                .build();
        byte[] answer = this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CardController.CREATE_CARD, 1)
                                .content(objectMapper.writeValueAsBytes(cardDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto card = objectMapper.readValue(answer, CardDto.class);
        assert card.getStatus().equals(CardStatusDto.ok().getStatus());
    }

    @Test
    void testAnnulStatus() throws Exception {
        CardStatusDto cardStatusDto = CardStatusDto.annulled();
        byte[] answer = this.mockMvc.perform(
                MockMvcRequestBuilders.post(CardController.UPDATE_CARD_STATUS, 1)
                        .content(objectMapper.writeValueAsBytes(cardStatusDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto card = objectMapper.readValue(answer, CardDto.class);
        assert card.getId() == 1;
        assert card.getStatus().equals(CardStatusDto.annulled().getStatus());
    }

    @Test
    void testExpireStatus() throws Exception {
        CardStatusDto cardStatusDto = CardStatusDto.expired();
        byte[] answer = this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CardController.UPDATE_CARD_STATUS, 1)
                                .content(objectMapper.writeValueAsBytes(cardStatusDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        CardDto card = objectMapper.readValue(answer, CardDto.class);
        assert card.getId() == 1;
        assert card.getStatus().equals(CardStatusDto.expired().getStatus());
    }

    @Test
    void testIncorrectUpdateStatus() throws Exception {
        CardStatusDto cardStatusDto = CardStatusDto.annulled();
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CardController.UPDATE_CARD_STATUS, 1)
                                .content(objectMapper.writeValueAsBytes(cardStatusDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CardController.UPDATE_CARD_STATUS, 1)
                                .content(objectMapper.writeValueAsBytes(cardStatusDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();

        cardStatusDto = CardStatusDto.expired();
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post(CardController.UPDATE_CARD_STATUS, 1)
                                .content(objectMapper.writeValueAsBytes(cardStatusDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
