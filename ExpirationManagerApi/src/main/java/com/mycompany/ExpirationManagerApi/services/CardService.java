package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.exceptions.*;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Отвечает за основную логику приложения касательно работы с банковскимии картами.
 */
@Service
@RequiredArgsConstructor
public class CardService {
    private static final Logger logger =
            Logger.getLogger(CardService.class.getName());

    private final CardRepository cardRepository;

    private final ClientService clientService;

    /**
     * Возвращает карту по заданому идентификатору.
     * @param id идентификатор искомой карты
     * @return Optional объект в котором будет содержаться искомая карта(при ее наличии в базе данных)
     */
    @Transactional
    public Optional<Card> findCard(Long id) {
        return cardRepository.findById(id);
    }

    /**
     * Возвращает карту по заданому номеру.
     * @param cardNumber номер искомой карты
     * @return Optional объект в котором будет содержаться искомая карта(при ее наличии в базе данных)
     */
    @Transactional
    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    /**
     * Возвращает карту по заданому идентификатору и генерирует исключение, если ее нет.
     * @param cardId идентификатор искомой карты
     * @return искомая карта
     * @throws NotFoundException, если карты с данным идентификатором не существует
     */
    @Transactional
    public Card findCardOrElseThrowException(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id '%s' doesn't exist", cardId))
                );
    }

    /**
     * Создает запись в базе данных о карте.
     * @param clientId идентификатор клиента-владельца карты
     * @param cardDto CardDto, содержащий необходимые для создания карты данные
     * @return сохраненная в базе данных карта
     * @throws NotFoundException, если клиента с заданным идентификатором не существует
     * @throws ValidationException, если карта имеет некорректный номер или даты выдычи и истечения срока.
     */
    @Transactional
    public Card createCard(Long clientId, @Valid CardDto cardDto) {
        Client client = clientService.findClientOrElseThrowException(clientId);
        validateIssueIsBeforeExpiration(cardDto.getDateOfIssue(), cardDto.getDateOfExpiration());

        if (cardDto.getCardNumber().isEmpty()) {
            cardDto.setCardNumber(generateUnusedCardNumber(cardDto.getCardNumber()));
        } else {
            if (!isCardNumberValid(cardDto.getCardNumber())) {
                ConstraintViolation cv = ConstraintViolation.builder()
                        .field("card_number")
                        .message("Проверьте правильность введенных данных")
                        .build();
                throw new ValidationException(
                        "Invalid card number",
                        Collections.singleton(cv)
                );
            }
            validateCardNumberIsUnique(cardDto.getCardNumber());
        }

        Card card = Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .dateOfIssue(cardDto.getDateOfIssue())
                .dateOfExpiration(cardDto.getDateOfExpiration())
                .status(CardStatus.OK)
                .client(client)
                .build();
        cardRepository.save(card);
        logger.info(String.format("Created card with id '%d'", card.getId()));
        return card;
    }

    /**
     * Обновляет статус карты. Допустимо изменение статуса с OK на ANNULLED или OK на EXPIRED.
     * @param cardId идентификатор карты, статус которой нужно изменить
     * @param cardStatusDto CardStatusDto, содержащий данные о новом статусе
     * @return карта с измененным статусом
     * @throws NotFoundException, если карты с заданным идентификатором не существует
     */
    @Transactional
    public Card updateCardStatus(Long cardId, CardStatusDto cardStatusDto) {
        Card card = findCardOrElseThrowException(cardId);
        if (card.getStatus() != CardStatus.OK)
            throw new InvalidCardStatusException(
                    String.format("Card '%s' cannot be '%s' due to it's status '%s'",
                        card.getCardNumber(),
                        cardStatusDto.getStatus(),
                        card.getStatus().toString())
            );
        card.setStatus(cardStatusDto.getStatus());
        cardRepository.save(card);
        logger.info(String.format("Card '%d' status was updated to %s", card.getId(), cardStatusDto.getStatus()));
        return card;
    }

    /**
     * Удаляет из базы данных запись о клиенте с заданным идентиикатором.
     * @param cardId идентификатор карты, которую нужно удалить
     * @throws NotFoundException, если карты с заданным идентификатором не существует
     */
    @Transactional
    public void deleteCard(Long cardId) {
        findCardOrElseThrowException(cardId);
        cardRepository.deleteById(cardId);
        logger.info("Deleted card with id '%d'".formatted(cardId));
    }

    /**
     * Возвращает список всех карт клиента с данным идентификатором.
     * @param clientId идентификатор клиента, карты которого нужно получить
     * @return список всех карт данного клиента
     * @throws NotFoundException, если клиента с заданным идентификатором не существует
     */
    @Transactional
    public List<Card> findAllByClient(Long clientId) {
        Client client = clientService.findClientOrElseThrowException(clientId);
        return cardRepository.findAllByClient(client);
    }

    /**
     * Возвращает список всех существующих в базе данных карт.
     * @return список всех существующих в базе данных карт
     */
    @Transactional
    public List<Card> findAll() {
        return cardRepository.findAllBy();
    }

    /**
     * Возвращает список карт с истекающим сроком.
     * Таковыми считаются карты, чья дата истечения наступит через месяц
     * относительно текущего(на момент вызова) дня и имеющие статус OK.
     * Используется для рассылки клиентам уведомлений об исчтечении их карт.
     * @return список карт с истекающим сроком
     */
    @Transactional
    public List<Card> findAllCloseToExpire() {
        return cardRepository.findAllCloseToDateOfExpiration(LocalDate.now(), Duration.ofDays(30));
    }

    /**
     * Возвращает список карт с истекшим сроком.
     * Таковыми считаются карты, чья дата истечения уже наступила и имеющие статус OK.
     * Используется для смены стаутса таких карт на EXPIRED
     * @return список карт с истекаюшим сроком
     */
    @Transactional
    public List<Card> findAllReadyToExpire() {
        return cardRepository.findAllByDateOfExpirationLessThan(LocalDate.now());
    }

    /**
     * Проверяет уникальность номера карты.
     * @param cardNumber номер, который нужно проверить.
     * @throws ValidationException, если карта с заданным номером уже есть в базе данных
     */
    @Transactional
    private void validateCardNumberIsUnique(String cardNumber) {
        if (cardRepository.findByCardNumber(cardNumber).isPresent()) {
            ConstraintViolation cv = ConstraintViolation.builder()
                    .field("card_number")
                    .message("Card Number must be a unique value")
                    .build();
            throw new ValidationException(
                    String.format("Card with number '%s' already exists", cardNumber),
                    Collections.singleton(cv)
            );
        }
    }

    /**
     * Проверяет, что дата выдачи не раньше даты истечения срока
     * @param dateOfIssue дата выдачи карты
     * @param dateOfExpiration  дата истечения срока карты
     * @throws ValidationException, если дата выдачи раньше даты истечения срока
     */
    private void validateIssueIsBeforeExpiration(LocalDate dateOfIssue, LocalDate dateOfExpiration) {
        if (dateOfIssue.isAfter(dateOfExpiration)) {
            ConstraintViolation cv = ConstraintViolation.builder()
                    .field("date_of_expiration")
                    .message("Date of expiration cannot be before date of issue")
                    .build();
            throw new ValidationException(
                    "Invalid dates provided",
                    Collections.singleton(cv)
            );
        }
    }

    //-----------------------------------------------------
    // CARD NUMBER
    //-----------------------------------------------------

    /**
     * Генерирует уникальный и еще не представленный в базе данных номер для карты.
     * @param prefix строка, с которой должен начинаться сгенерированный номер
     * @return сгенерированный номер карты
     */
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
     * Проверяет правильность номера карты согласно алгоритма Луна.
     * Подразумевается, что строка состоит из десятичный цифр.
     * Правильной считается строка, состоящая из 16 символов и отвечающая проверке алгоритмом Луна.
     * @param cardNumber строка, которую нужно проверить
     * @return true, если результат проверки положительный, иначе - false
     */
    public Boolean isCardNumberValid(String cardNumber) {
        if (cardNumber.length() != 16)
            return false;
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            char tmp = cardNumber.charAt(i);
            int num = tmp - '0';
            if (i % 2 == 0) {
                num *= 2;
                if (num > 9)
                    num -= 9;
            }
            sum += num;
        }
        return sum % 10 == 0;
    }

    /**
     * Генерирует номер карты с заданным префиксом.
     * @param prefix подстрока, с которой будет начинаться номер карты (1-15 символов)
     * @return сгенерированный согласно алгоритму Луна номер карты
     */
    public String generateCardNumber(String prefix) {
        if (prefix.length() > 15)
            throw new IllegalArgumentException("Card number can not contain more than 16 digits");
        //actually card number has 16 digits(in general), but one is reserved to be the check digit
        int wantedLength = 15;

        // up to 9 digits
        String nanoSeconds = String.valueOf(Instant.now().getNano());
        String seconds = String.valueOf(Instant.now().getEpochSecond());
        String combined = prefix+nanoSeconds+seconds;
        if (combined.length() < wantedLength) {
            combined += "0".repeat(wantedLength - combined.length());
        }
        else if (combined.length() > wantedLength) {
            combined = combined.substring(0, wantedLength);
        }
        return combined + generateCheckDigit(combined);
    }

    /**
     * Вспомогательная функция для вычисления контрольной цифры.
     * Используется при генериации номера карты.
     * @param cardNumber номер карты, для которого нужно вычислить контрольную цифру
     * @return символ контрольной цифры
     */
    private char generateCheckDigit(String cardNumber) {
        int sum = 0;
        int mod = cardNumber.length() % 2;
        for (int i = 0; i < cardNumber.length(); i++) {
            int num = cardNumber.charAt(i) - '0';
            if (i % 2 != mod) {
                num *= 2;
                if (num > 9)
                    num -= 9;
            }
            sum += num;
        }
        return (char)((10 - (sum % 10)) % 10 + '0');
    }
}
