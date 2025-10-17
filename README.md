# VK Wall Parser

Парсер стен `ВКонтакте` с сохранением постов в `MongoDB`.

## Описание

Проект подключается к `API ВКонтакте`, получает посты со стены указанного 
пользователя или сообщества и сохраняет их в базу данных `MongoDB`.

Используется:
- `Java 21` + `Spring Framework` (без `Spring Boot`)
- `Spring Data MongoDB` (`MongoTemplate`)
- `Docker Compose` для локального запуска `MongoDB` и `Mongo Express`
- `.env` файл для хранения конфиденциальных данных

### Подготовка и запуск

1. Создайте `.env` файл с секретами по примеру `.env.example`, 
обязательно указав ваш `ACCESS_TOKEN`
2. Укажите `id` пользователей или сообществ в формате и необходимом количестве: `groups=groupId1,groupId2` 
3. Запустите MongoDB через Docker Compose: `docker compose up -d`
4. Скомпилируйте проект: `mvn clean compile` 
5. Запустите проект: `mvn exec:java`