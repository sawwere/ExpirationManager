package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class NotificationService {
    static final Logger logger =
            Logger.getLogger(NotificationService.class.getName());

    private final ClientService clientService;
    private final CardService cardService;
    private final CustomEmailService emailService;

    public String generateExpirationMessage(Client client, Card card) {
        LocalDate dateOfExpiration = card.getDateOfExpiration();
        String niceDate = String.format("%d.%d.%d",
                dateOfExpiration.getDayOfMonth(),
                dateOfExpiration.getMonthValue(),
                dateOfExpiration.getYear()
        );
        String hidedCardNumber = String.format("****-%s",
                card.getCardNumber().substring(card.getCardNumber().length() - 5));
        return String.format("%s, срок вашей карты %s заканчивается %s, обратитесь в ближайшее отделение для получения новой карты.",
                client.getFirstName(), hidedCardNumber, niceDate);
    }

    @Async
    @Scheduled(fixedRateString = "${interval}")
    public void sendNotificationsAboutCardExpiration() throws InterruptedException {
        List<Card> closeToExpire = cardService.findAllCloseToExpire();
        for (Card card : closeToExpire) {
            //TODO
            Client client = clientService.findClient(card.getClient().getId()).get();
            logger.log(Level.FINE, String.format("Send email to" , client.getEmail()));
            emailService.sendSimpleEmail(
                    client.getEmail(),
                    "Истечение срока карты",
                    generateExpirationMessage(client, card));
        }
    }
}
