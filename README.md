Eventify — Fullstack система бронирования мероприятий
Это комплексная платформа для организации событий, включающая современный фронтенд на React и мощный бэкенд на Spring Boot.

🏗 Структура проекта
/eventify-backend: Серверная часть (Java, Spring Boot, PostgreSQL, Telegram Bot).
/eventify-frontend: Клиентская часть (React, TypeScript, Tailwind CSS).
🚀 Быстрый запуск (Docker Compose)
Самый простой способ запустить всю систему целиком — использовать Docker.

Клонируйте репозиторий:
  bash
  git clone https://github.com/zarinakanataeva/eventify.git
  cd eventify
Запустите все сервисы:
bash
docker-compose up --build
После запуска:

Фронтенд доступен на: http://localhost:3000
Бэкенд API доступен на: http://localhost:8080
Swagger (документация API): http://localhost:8080/swagger-ui.html
🛠 Технологический стек
Бэкенд
Java 17 / Spring Boot 3
Spring Security & JWT (безопасность)
PostgreSQL & Liquibase (база данных и миграции)
Telegram Bot API (уведомления)
Springdoc OpenAPI / Swagger UI (документация API)
Фронтенд
React 18 / TypeScript
Tailwind CSS (дизайн)
Axios (запросы)
React Hook Form (валидация)
API Spec: OpenAPI 3.1.0 (Swagger)
📖 Документация API
В проекте используется спецификация OpenAPI

Этот файл можно импортировать в Postman или Swagger Editor для тестирования всех эндпоинтов (Auth, Bookings, Events, Admin и др.).

📖 Подробные инструкции

Инструкции по Бэкенду
Инструкции по Фронтенду
