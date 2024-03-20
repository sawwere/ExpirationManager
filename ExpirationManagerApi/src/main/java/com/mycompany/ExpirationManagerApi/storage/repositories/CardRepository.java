package com.mycompany.ExpirationManagerApi.storage.repositories;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Служит для организации доступа к сущностям типа БАНКОВСКАЯ КАРТА в базе данных.
 */
public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * Возвращает карту по ее номеру.
     * @param cardNumber номер искомой карты
     * @return Optional объект в котором будет содержаться искомая карта(при ее наличии в базе данных)
     */
    Optional<Card> findByCardNumber(String cardNumber);

    /**
     * Возвращает список всех существующих в базе данных карт.
     * @return список всех существующих в базе данных карт
     */
    List<Card> findAllBy();

    /**
     * Возвращает список всех карт клиента.
     * @param client клиент, карты которого нужно получить
     * @return список всех карт данного клиента
     */
    List<Card> findAllByClient(Client client);

    /**
     * Возвращает список карт с датой истечения меньшей заданной, имеющих статус OK.
     * Также подгружает их владельцев-клиентов.
     * @param date дата, меньше которой должны быть даты истечения срока карт
     * @return список карт с подгруженными клиентами
     */
    @Query(""" 
                SELECT c FROM Card c
                JOIN FETCH c.client
                WHERE c.status = 'OK'
                    AND c.dateOfExpiration - :date < 0""")
    List<Card> findAllByDateOfExpirationLessThan(LocalDate date);

    /**
     * Возвращает список карт у которых дата истечения наступит через заданное количество дней после заданной даты, имеющих статус OK.
     * Также подгружает их владельцев-клиентов.
     * @param date дата, относительно которой нужно произвести расчет
     * @param daysUntilExpiration через сколько дней после этого наступит дата истечения
     * @return список карт с подгруженными клиентами
     */
    @Query(""" 
                SELECT c FROM Card c
                JOIN FETCH c.client
                WHERE c.status = 'OK'
                    AND c.dateOfExpiration - :date = :daysUntilExpiration""")
    List<Card> findAllCloseToDateOfExpiration(LocalDate date, Duration daysUntilExpiration);
}
