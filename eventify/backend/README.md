# Event Booking Backend

Бэкенд-сервис для управления мероприятиями, бронированиями и уведомлениями.

## Технологии
- Spring Boot 3
- PostgreSQL
- Spring Security (JWT)
- Telegram Bot SDK
- Liquibase

## Локальный запуск (без Docker)
1. Установите JDK 17 и PostgreSQL.
2. Создайте базу данных `event_booking`.
3. Настройте `application.yml` (укажите логин/пароль к БД).
4. Запустите проект:
```
 bash
   ./mvnw spring-boot:run
```

 API Эндпоинты
Все эндпоинты задокументированы в Swagger: 
```
 http://localhost:8080/swagger-ui.html
```
