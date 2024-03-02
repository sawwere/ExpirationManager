package com.mycompany.ExpirationManagerApi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerApi.Application;
import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.factories.ClientDtoFactory;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create_clients.sql", "/sql/create_cards.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/cards_after.sql", "/sql/clients_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientServiceTests {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientService clientService;

    private final ClientDtoFactory clientDtoFactory = new ClientDtoFactory();

    @BeforeAll
    public static void beforeAll() {
    }

    @Test()
    void testDelete() throws Exception{
        Assertions.assertThrows(NotFoundException.class, () -> clientService.deleteClient(0L));

        clientService.deleteClient(2L);
    }

    @Test
    void testFind() throws Exception {
        assert clientService.findClient(120L).isEmpty();

        Optional<Client> optionalClient = clientService.findClient(0L);
        assert optionalClient.isEmpty();

        optionalClient = clientService.findClient(1L);
        assert optionalClient.isPresent();
        assert optionalClient.get().getId().equals(1L);
        assert optionalClient.get().getBirthday().equals(LocalDate.parse("2020-01-06"));
        assert optionalClient.get().getFirstName().equals("Burundi");
        assert optionalClient.get().getLastName().equals("Koch");
        assert optionalClient.get().getEmail().equals("sigma@mmm.mmm");
        assert optionalClient.get().getPassport().equals("6010000000");
        assert optionalClient.get().getPatronymicName().equals("");
    }

    @Test
    void testCreate() throws Exception {
        ClientDto clientToCreate = ClientDto.builder()
                .birthday(LocalDate.parse("1998-01-06"))
                .firstName("Kinder".toUpperCase())
                .lastName("Penguin".toUpperCase())
                .email("cd@google.uk")
                .passport("1233211234")
                .patronymicName("Abram".toUpperCase())
                .build();
        Client client = clientService.createClient(clientToCreate);
        clientToCreate.setId(client.getId());
        assert clientToCreate.equals(clientDtoFactory.make(client));
    }
}
