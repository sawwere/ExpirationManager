package com.mycompany.ExpirationManagerApi.storage.repositories;

import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.stream.Stream;

import java.util.List;
import java.util.Optional;

/**
 * Служит для организации доступа к сущностям типа КЛИЕНТ в базе данных.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    /**
     * Возвращет клиента по пасспорту
     * @param passport пасспртные данные искомого клиента
     * @return Optional объект в котором будет содержаться искомый клиент(при его наличии в базе данных)
     */
    Optional<Client> findByPassport(String passport);

    /**
     * Возвращает первого найденного в базе клиента, имеющего заданные пасспортные данные ИЛИ почтовый адрес.
     * @param passport пасспортные данные для поиска
     * @param Email почтовый адрес для поиска
     * @return Optional объект в котором будет содержаться искомый клиент(при его наличии в базе данных)
     */
    Optional<Client> findFirstByPassportOrEmail(String passport, String Email);

    /**
     * Возвращает последовательность из всех имеющихся в базе данных клиентов
     * @return последовательность из всех имеющихся в базе данных клиентов
     */
    Stream<Client> streamAllBy();
}
