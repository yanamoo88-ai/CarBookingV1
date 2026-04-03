🚗 𝓒𝓪𝓻 𝓡𝓮𝓷𝓽𝓪𝓵 𝓐𝓹𝓹 🚗
Проект по автоматизации тестирования • Test Plan • UI + API + DB Testing
✨ Обзор проекта
Car Rental App --- это веб-приложение для бронирования и аренды автомобилей. Цель данного проекта --- создать единый automation framework для тестирования веб-приложения Car Rental и проверить полный бизнес-процесс как для клиента, так и для администратора.

Проект включает:

• UI тестирование с использованием Selenium WebDriver • API тестирование с использованием Rest Assured • Проверку базы данных с использованием JDBC + MySQL • Единый automation framework на Java + TestNG • Сквозную проверку сценариев для ролей Client и Admin

🎯 Цели проекта
• Проверить все основные пользовательские сценарии • Обеспечить согласованность между UI, API и базой данных • Сократить время ручной регрессии • Выявлять дефекты на раннем этапе разработки • Создать масштабируемую и поддерживаемую автоматизацию

💡 Почему были выбраны именно эти технологии?
Технология Причина использования

Java 21+ Современный и стабильный язык для автоматизации

TestNG Гибкие test suites, параллельный запуск, аннотации

Selenium Надёжное UI тестирование в разных браузерах WebDriver

Rest Assured Удобная проверка API и schema validation

JDBC Прямая проверка сохранённых данных в базе данных

Gradle Удобная сборка проекта и управление зависимостями

Allure + Jenkins Формирование отчётов и интеграция с CI/CD
🚧 Сложности, выявленные в проекте
Во время работы над проектом были обнаружены следующие проблемы:

• Некоторые endpoints из User Stories отсутствуют в Swagger (PATCH /users/me, POST /logout) • Нестабильная локальная среда и изменение API contract • Зависимость тестов от состояния базы данных и токенов авторизации • Необходимость синхронизировать проверки UI, API и DB внутри одного framework

🔗 Окружение
Окружение URL

Frontend http://localhost:5173/

API v3 https://dev.pshacks.org/api/v3/

База данных MySQL
🎯 Цель тестирования
Тестирование должно подтвердить, что:

✅ Клиент может зарегистрироваться и войти в систему ✅ Клиент может просматривать доступные автомобили и категории ✅ Клиент может создавать и отменять бронирования ✅ Клиент может просматривать историю своих бронирований ✅ Клиент может оставлять и просматривать отзывы ✅ Администратор может управлять бронированиями ✅ Администратор может изменять статус автомобиля ✅ API возвращает корректные данные ✅ UI, API и база данных работают согласованно в рамках единого automation framework

🧭 Основной пользовательский поток
Регистрация / Логин ↓ Профиль ↓ Автомобили и категории ↓ Бронирование ↓ История бронирований ↓ Обработка администратором ↓ Отзывы

✨ Обзор проекта
Car Rental App --- это веб-приложение для аренды автомобилей. Проект включает:

• UI тестирование • API тестирование • Проверку базы данных • Интеграцию automation framework • Проверку бизнес-процессов для ролей Client и Admin

🔗 Окружение
Окружение URL

Frontend http://localhost:5173/

API v3 https://dev.pshacks.org/api/v3/

База данных MySQL
🧪 Область тестирования
✅ Входит в scope
• Регистрация • Логин / Лог аут • Страница профиля • Страница автомобилей • Категории автомобилей • Создание бронирования • История бронирований • Отмена бронирования • Отзывы • API endpoints v3 • Проверка согласованности данных в базе данных • Авторизация и работа с токенами • Интеграция UI + API + DB

❌ Не входит в scope
• Платёжные системы • Мобильное приложение • Email / SMS уведомления • Сторонние интеграции • Полное accessibility testing

⚙️ Установка и запуск
Предварительные требования
Перед запуском проекта необходимо установить:

• Java 21+ • Gradle 9.3.0+ • Chrome / Firefox / Safari • MySQL • Git

Клонирование репозитория
git clone git@github.com:vitrom7777/CarBookingV1.git

Настройка окружения
Создайте или обновите конфигурационный файл:

BASE_URL=http://localhost:5173/ API_URL=https://dev.pshacks.org/api/v3/ DB_URL=jdbc:mysql://localhost:3306/car_rental DB_USER=root DB_PASSWORD=your_password

Сборка проекта
./gradlew clean build

Запуск всех тестов
./gradlew test

Запуск конкретного набора тестов
./gradlew test -DsuiteXmlFile=testng.xml

🖥️ Тестовое окружение
Параметр Значение

Frontend http://localhost:5173/

API https://dev.pshacks.org/api/v3/

Поддерживаемые браузеры Chrome, Firefox, Safari

Операционные системы Windows 11, macOS

База данных MySQL

🧰 Технологии и инструменты
Инструмент Назначение

Java 21 Основной язык автоматизации

Gradle Сборка и запуск проекта

TestNG Управление test suites

Selenium UI автоматизация WebDriver

Rest Assured API автоматизация

JDBC Проверка базы данных

MySQL Тестовая база данных

Lombok Сокращение boilerplate кода

Logback Логирование

GitHub Контроль версий

Trello Управление дефектами

🖥️ Поддерживаемые браузеры
• Chrome • Firefox • Safari

Поддерживаемые операционные системы:

• Windows 11 • macOS

📷 Скриншоты и документация
Swagger API Documentation
• Базовый URL API: https://dev.pshacks.org/api/v3/ • Swagger и Postman collection можно добавить в репозиторий в папки:

docs/swagger docs/postman

Полезные ссылки
• Selenium Documentation: https://www.selenium.dev/documentation/ • Rest Assured Documentation: https://rest-assured.io/ • TestNG Documentation: https://testng.org/ • Allure Report: https://docs.qameta.io/allure/ • HTTP Status 100 Reference: https://http.cat/status/100

👨‍💻 Команда QA
Роль Участник Ответственность

QA Lead Yana Yerusalymska Test Plan, TestLink, Trello, отчётность

QA API Vitalii Romanskyi API,DB testing, Postman, Rest Assured

QA Manual Alexandr Karpov Ручное тестирование, диаграммы состояний

QA GUI Oleh Hanziienko UI automation, Jenkins, Allure

QA Manual Tetiana Nosenko Ручное тестирование, Mind Maps

⚠️ Риски
• Нестабильная QA / local среда
• Изменение API contract
• Задержка разработки frontend или backend
• Недостаток тестовых данных
• Отсутствие доступа к admin functionality
• Несогласованность данных между UI, API и DB
• Нестабильные тесты из-за состояния базы данных
• Проблемы с токенами авторизации

🚀 Запуск тестов
./gradlew clean test

Запуск конкретного набора тестов:

./gradlew test -DsuiteXmlFile=testng.xml

🌟 «Качество в движении. Тестируй всё.» 🌟
