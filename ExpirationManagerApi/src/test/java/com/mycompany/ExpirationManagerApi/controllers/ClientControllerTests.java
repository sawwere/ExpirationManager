package com.mycompany.ExpirationManagerApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.services.ClientService;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create_clients.sql", "/sql/create_cards.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/cards_after.sql", "/sql/clients_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;





    @Test
    void testGetAll() throws Exception{

        byte[] answer = this.mockMvc.perform(get(ClientController.FIND_ALL_CLIENTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        ClientDto[] clients = objectMapper.readValue(answer, ClientDto[].class);
        assert clients.length == 5;
    }

    @Test
    void testDelete() throws Exception{
        this.mockMvc.perform(delete(ClientController.DELETE_CLIENT, 0))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(delete(ClientController.DELETE_CLIENT, 2))
                .andExpect(status().is2xxSuccessful());

        byte[] answer = this.mockMvc.perform(get(ClientController.FIND_ALL_CLIENTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        ClientDto[] clients = objectMapper.readValue(answer, ClientDto[].class);
        assert clients.length == 4;
        assert Arrays.stream(clients).noneMatch(x -> x.getId() == 2);
    }

    @Test
    void testFind() throws Exception {
        byte[] answer = this.mockMvc.perform(get(ClientController.FIND_CLIENT, 1))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsByteArray();
        ClientDto client = objectMapper.readValue(answer, ClientDto.class);
        assert client.getId() == 1;
        assert client.equals(ClientDto.builder()
                .id(1L)
                .birthday(LocalDate.parse("2020-01-06"))
                .firstName("Burundi")
                .lastName("Koch")
                .email("sigma@mail.com")
                .passport("6010-000000")
                .patronymicName("")
                .build());
    }

    @Test
    void testCreate() throws Exception {
        ClientDto clientToCreate = ClientDto.builder()
                .birthday(LocalDate.parse("1998-01-06"))
                .firstName("Kinder")
                .lastName("Penguin")
                .email("cd@google.uk")
                .passport("123321456")
                .patronymicName("Abram")
                .build();

        byte[] answer = this.mockMvc.perform(
                        MockMvcRequestBuilders.post(ClientController.CREATE_CLIENT)
                                .content(objectMapper.writeValueAsBytes(clientToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        ClientDto client = objectMapper.readValue(answer, ClientDto.class);
        clientToCreate.setId(client.getId());
        assert clientToCreate.equals(client);
    }


}
