package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create_clients.sql", "/sql/create_cards.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/cards_after.sql", "/sql/clients_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardServiceTests {
    @Autowired
    CardService cardService;
    @Test
    void testIsCardNumberValid() {
        String cardNumber = "6918456001709395";
        assert cardService.isCardNumberValid(cardNumber);
        cardNumber = "454975300170938";
        assert !cardService.isCardNumberValid(cardNumber);
        cardNumber = "4549753001709383";
        assert !cardService.isCardNumberValid(cardNumber);
    }

    @Test
    void testGenerateCardNumber() {
        String prefix = "";
        assert cardService.isCardNumberValid(cardService.generateCardNumber(prefix));
        prefix = "482";
        assert cardService.isCardNumberValid(cardService.generateCardNumber(prefix));
        prefix = "12";
        assert cardService.isCardNumberValid(cardService.generateCardNumber(prefix));
        prefix = "454975300170938";
        assert cardService.isCardNumberValid(cardService.generateCardNumber(prefix));
    }
}
