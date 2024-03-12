# ExpirationManager
Задача  
Автоматическая рассылка уведомление клиентам банка о завершении срока действий их банковских карт  
Есть объекты:  
- Клиент (ФИО, Дата рождения, …)
- Банковская карта (Клиент, Номер карты, Дата выдачи, Дата окончания действия, …)

Разработать задание по расписанию, которое будет осуществлять рассылку уведомления клиентам при завершении срока действия банковской карты. Дополнительно предусмотреть следующие функции:   
1.	Создание клиента  
Если клиент уже был раннее заведен, возвращать его идентификатор, чтобы избежать дублей
2.	Создание карты  
Заводит новую карту клиенту (опционально, можно проверять уникальность номера карты при заведении)
3.	Аннулирование карты  
Закрывает действующую карту клиента

 ## ExpirationManagerApi
 Cервис для работы с клиентами и банковскими картами, в операциях участвуют объекты Client, Card, CardStatus, ErrorInfo  
 ### Client
 Объект для работы с клиентом.
 ```json
 {
    "id": 1,  
    "passport": "0000000000",  
    "email": "example@gmail.ru",  
    "birthday": "2020-01-06",  
    "first_name": "Tom",  
    "last_name": "Skier",  
    "patronymic_name": ""
}
```
### Card
 Объект для работы с банковскими картами,  возможные значения статуса: OK, ANNULLED, EXPIRED
```json
{
    "id": 2,
    "status": "ANNULLED",
    "card_number": "0000000000000000",
    "date_of_issue": "2024-06-06",
    "date_of_expiration": "2028-10-11"
 }
```
### CardStatus
Объект для изменения статуса банковских карт, возможные значения: OK, ANNULLED, EXPIRED
```json
{
    "status" : "ANNULLED"
}
```
### ErrorInfo 
Объект, возвращаемый в случае возниковения ошибок в ходе выполнения запросов
```json
{
    "error": "Validation error",
    "description": "clientDto",
    "constraint_violations": [
        {
            "field": "lastName",
            "message": "Фамилия должна состоять из латинских или кириллических символов"
        }
    ]
}
```
---
## Список поддерживаемых запросов
### Получить список всех клиентов    
 - **GET** **/api/clients**
 - Response: список Client
### Создать клиента
 - **POST**  **/api/clients**  
- Request Body: application/json, Client(опционально может отсутствовать отчество)  
- Response: Client  

### Получить клиента 
 - **GET**  **/api/clients/{client_id}**  
 - Параметры: {client_id} - id клиента
 - Response: Client

### Удалить клиента по заданному id
 - **DELETE**  **/api/clients/{client_id}**  
 - Параметры: {client_id} - id клиента
 - Response: NO_CONTENT

### Получить список всех карт
 - **GET**  **/api/cards**
 - Response: список Card

### Получить список всех карт клиента
 - **GET**  **/api/clients/{client_id}/cards**  
 - Параметры: {client_id} - id клиента
 - Response: список Card

### Добавить клиенту новую карту
 - **POST**  **/api/clients/{client_id}/cards**
 - Параметры: {client_id} - id клиента
 - Request Body: application/json, Card(опционально можно передать пустую строку в качестве номера карты для автогенерации)
 - Response: Card

### Получить карту по заданному id
 - **GET**  **/api/cards/{card_id}**  
 - Параметры: {card_id} - id карты
 - Response: Card

### Удалить карту по заданному id
 - **DELETE** **/api/cards/{card_id}**  
 - Параметры: {card_id} - id карты
 - Response: NO_CONTENT

### Изменить статус карты
 - **POST** **/api/cards/{card_id}/status**
 - Параметры: {card_id} - id карты
 - Request Body: application/json, CardStatus  
 - Response: Card
## ExpirationManagerWeb