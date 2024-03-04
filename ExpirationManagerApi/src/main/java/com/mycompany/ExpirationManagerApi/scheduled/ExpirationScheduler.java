package com.mycompany.ExpirationManagerApi.scheduled;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.services.CardService;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ExpirationScheduler {
    private static final Logger logger =
            Logger.getLogger(ExpirationScheduler.class.getName());

    private final CardService cardService;

    @Transactional
    public Card generateNewCard(long clientId, Card card) {
        CardDto newCard = CardDto.builder()
                .cardNumber(cardService.generateUnusedCardNumber(""))
                .dateOfIssue(LocalDate.now())
                .dateOfExpiration(LocalDate.now().plusYears(4))
                .status(CardStatus.OK.toString())
                .build();
        return cardService.createCard(clientId, newCard);
    }

    @Async
    @Scheduled(fixedRateString = "${interval}")
    public void makeCardsExpired() {
        List<Card> readyToExpire = cardService.findAllReadyToExpire();
        logger.info(String.format("Cards to be set expired: %d", readyToExpire.size()));
        for (Card card : readyToExpire) {
            cardService.updateCardStatus(card.getId(), CardStatusDto.expired());
            Card newCard = generateNewCard(card.getClient().getId(), card);
            logger.info("Generated new card %s to replace the old %s"
                    .formatted(newCard.getCardNumber(), card.getCardNumber()));
        }
    }
}
