package com.mycompany.ExpirationManagerApi.scheduled;

import com.mycompany.ExpirationManagerApi.services.CardService;
import com.mycompany.ExpirationManagerApi.services.ClientService;
import com.mycompany.ExpirationManagerApi.services.CustomEmailService;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private static final Logger logger =
            Logger.getLogger(NotificationScheduler.class.getName());

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
    public void sendNotificationsAboutCardExpiration() {
        List<Card> closeToExpire = cardService.findAllCloseToExpire();
        System.out.println(closeToExpire.size());
        for (Card card : closeToExpire) {
            Client client = card.getClient();
            logger.log(Level.INFO, String.format("Send email to %s" , client.getEmail()));
            emailService.sendSimpleEmail(
                    client.getEmail(),
                    "Истечение срока карты",
                    generateExpirationMessage(client, card));
        }
    }
}