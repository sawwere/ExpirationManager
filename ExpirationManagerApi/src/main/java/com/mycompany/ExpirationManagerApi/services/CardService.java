package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.exceptions.CustomException;
import com.mycompany.ExpirationManagerApi.exceptions.InvalidCardStatusException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final Logger logger =
            Logger.getLogger(CardService.class.getName());

    private final CardRepository cardRepository;

    private final ClientService clientService;

    @Transactional
    public Optional<Card> findCard(Long id) {
        return cardRepository.findById(id);
    }

    @Transactional
    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    @Transactional
    public Card findCardOrElseThrowException(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id '%s' doesn't exist", cardId))
                );
    }

    @Transactional
    public Card createCard(Long clientId, CardDto cardDto) {
        var optionalClient = clientService.findClient(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException(
                    String.format("Client with id '%s' doesn't exist", clientId)
            );
        }
        if (cardRepository.findByCardNumber(cardDto.getCardNumber()).isPresent()) {
            throw new CustomException(
                    String.format("Card with number '%s' already exists", cardDto.getCardNumber())
            );
        }
        if (cardDto.getCardNumber().length() < 16) {
            cardDto.setCardNumber(generateUnusedCardNumber(cardDto.getCardNumber()));
        }
        else if (isCardNumberValid(cardDto.getCardNumber())) {
            throw new CustomException(
                    String.format("Card number '%s' is not valid", cardDto.getCardNumber())
            );
        }
        Card card = Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .dateOfIssue(cardDto.getDateOfIssue())
                .dateOfExpiration(cardDto.getDateOfExpiration())
                .status(CardStatus.OK)
                .client(optionalClient.get())
                .build();
        cardRepository.save(card);
        logger.info(String.format("Created card with number '%s'", card.getCardNumber()));
        return card;
    }

    @Transactional
    public Card updateCardStatus(Long cardId, CardStatusDto cardStatusDto) {
        if (!cardStatusDto.isValid())
            throw new InvalidCardStatusException(
                    String.format("Invalid card status '%s' was given", cardStatusDto.getStatus())
            );
        Card card = findCardOrElseThrowException(cardId);
        if (card.getStatus() != CardStatus.OK)
            throw new InvalidCardStatusException(
                    String.format("Card '%s' cannot be '%s' due to it's status '%s'",
                        card.getCardNumber(),
                        cardStatusDto.getStatus(),
                        card.getStatus().toString())
            );
        card.setStatus(cardStatusDto.getCardStatus());
        cardRepository.save(card);
        logger.info(String.format("Card '%d' status was updated to %s", card.getId(), cardStatusDto.getStatus()));
        return card;
    }

    @Transactional
    public void deleteCard(Long cardId) {
        findCardOrElseThrowException(cardId);
        logger.info(String.format("Card '%d' was deleted", cardId));
        cardRepository.deleteById(cardId);
    }

    @Transactional
    public List<Card> findAllByClient(Long clientId) {
        Client client = clientService.findClientOrElseThrowException(clientId);
        return cardRepository.findAllByClient(client);
    }

    @Transactional
    public List<Card> findAll() {
        return cardRepository.findAllBy();
    }

    @Transactional
    public List<Card> findAllCloseToExpire() {
        return cardRepository.findAllCloseToExpirationByDuration(LocalDate.now(), Duration.ofDays(30));
    }

    @Transactional
    public List<Card> findAllReadyToExpire() {
        return cardRepository.findAllCloseToExpirationByDuration(LocalDate.now(), Duration.ofDays(0));
    }

    //-----------------------------------------------------
    // CARD NUMBER
    //-----------------------------------------------------
    @Transactional
    public String generateUnusedCardNumber(String prefix) {
        String newCardNumber = generateCardNumber(prefix);
        int collissionCounter = 0;
        while (findByCardNumber(newCardNumber).isPresent()) {
            newCardNumber = generateCardNumber("");
            collissionCounter++;
        }
        logger.fine("Collision happened %d times during CardNumber generation".formatted(collissionCounter));
        return newCardNumber;
    }

    /**
     * @param cardNumber string to be validated
     * @return result of applying validation Luhn algorithm
     */
    public Boolean isCardNumberValid(String cardNumber) {
        if (cardNumber.length() != 16)
            return false;
        int sum = 0;
        for (int i = cardNumber.length()-1; i >= 0; i--) {
            char tmp = cardNumber.charAt(i);
            int num = tmp - '0';
            if (i % 2 == 0) {
                num *= 2;
            }
            sum += num;
        }
        return sum % 10 == 0;
    }


    /**
     * @param prefix generated card number starts with that string (1-15 symbols)
     * @return card number generated with Luhn algorithm
     */
    public String generateCardNumber(String prefix) {
        if (prefix.length() > 15)
            throw new IllegalArgumentException("Card number can not contain more than 16 digits");
        //actually card number has 16 digits(in general), but one is reserved to be the check digit
        int neededLength = 15 - prefix.length();

        // up to 9 digits
        String nanoSeconds = String.valueOf(Instant.now().getNano());
        String seconds = String.valueOf(Instant.now().getEpochSecond());
        String combined = nanoSeconds+seconds;
        if (combined.length() < neededLength) {
            combined += "0".repeat(neededLength - combined.length());
        }
        else if (combined.length() > neededLength) {
            combined = combined.substring(0, neededLength);
        }
        int sum = 0;
        for (int i = neededLength-1; i >= 0; i--) {
            int num = combined.charAt(i) - '0';
            if (i % 2 == 0) {
                num *= 2;
            }
            sum += num;
        }
        int s = (10 - (sum % 10)) % 10;
        return prefix + combined + s;
    }
}
