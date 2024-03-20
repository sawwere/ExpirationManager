package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.ConstraintViolation;
import com.mycompany.ExpirationManagerApi.exceptions.ValidationException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Отвечает за основную логику приложения касательно работы с клиентами.
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    private static final Logger logger =
            Logger.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;

    /**
     * Возвращает клиента по заданому идентификатору.
     * @param id идентификатор искомого клиента
     * @return Optional объект в котором будет содержаться искомый клиент(при его наличии в базе данных)
     */
    @Transactional
    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }

    /**
     * Возвращает клиена по заданому идентификатору и генерирует исключение, если его нет.
     * @param clientId идентификатор искомого клиента
     * @return искомый клиент
     * @throws NotFoundException, если клиента с данным идентификатором не существует
     */
    @Transactional
    public Client findClientOrElseThrowException(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' doesn't exist", clientId))
                );
    }

    /**
     * Создает запись в базе данных о клиенте.
     * @param clientDto ClientDto, содержащий необходимые для создания карты данные.
     *                  Может отстутсовать поле patronymicName(отчество)
     * @return сохраненный в базе данных клиент
     * @throws ValidationException, если клиент имеет неуникальный адрес почты или паспортные данные
     */
    @Transactional
    public Client createClient(@Valid ClientDto clientDto) {
        validatePassportAndEmailAreUnique(clientDto);
        Client client = Client.builder()
                .passport(clientDto.getPassport())
                .email(clientDto.getEmail())
                .firstName(clientDto.getFirstName().toUpperCase())
                .lastName(clientDto.getLastName().toUpperCase())
                .patronymicName(clientDto.getPatronymicName().toUpperCase())
                .birthday(clientDto.getBirthday())
                .build();
        clientRepository.save(client);
        logger.info("Created client with id '%d'".formatted(client.getId()));
        return client;
    }

    /**
     * Удаляет из базы данных запись о клиенте с заданным идентиикатором.
     * @param clientId идентификатор клиента, которого нужно удалить
     * @throws NotFoundException, если клиента с заданным идентификатором не существует
     */
    @Transactional
    public void deleteClient(Long clientId) {
        Client client = findClientOrElseThrowException(clientId);
        clientRepository.deleteById(clientId);
        logger.info("Deleted client with id '%d'".formatted(clientId));
    }

    /**
     * Возвращает список всех существующих в базе данных клиентов.
     * @return список всех существующих в базе данных клиентов
     */
    @Transactional
    public List<Client> findAll() {
        return clientRepository.streamAllBy().toList();
    }

    /**
     * Проверяет пасспортные данные и адрес электронной почты клиента на уникальность.
     * @param clientDto клиент, чьи данные нужно проверить
     * @throws ValidationException, если клиент имеет неуникальный адрес почты или паспортные данные
     */
    private void validatePassportAndEmailAreUnique(ClientDto clientDto) {
        var optionalClient = clientRepository.findFirstByPassportOrEmail(clientDto.getPassport(), clientDto.getEmail());
        if (optionalClient.isPresent()) {
            ArrayList<ConstraintViolation> cvList = new ArrayList<>(2);
            if (clientDto.getPassport().equals(optionalClient.get().getPassport()))
            {
                cvList.add(ConstraintViolation.builder()
                        .field("passport")
                        .message("Passport must be a unique value")
                        .build());

            }
            if (clientDto.getEmail().equals(optionalClient.get().getEmail()))
            {
                cvList.add(ConstraintViolation.builder()
                        .field("email")
                        .message("Email must be a unique value")
                        .build());
            }
            throw new ValidationException(
                    String.format("Client with given info already exists with id '%d'",
                            optionalClient.get().getId()),
                    cvList
            );
        }
    }
}
