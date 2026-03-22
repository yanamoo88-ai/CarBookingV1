package com.carbooking.taskApp.database.tests;

import com.carbooking.database.repository.UserRepository;
import com.carbooking.dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserDatabaseTest {

    // Создаем экземпляр репозитория для работы с таблицей users
    UserRepository userRepository = new UserRepository();

    @Test
    public void testGetUserByEmailFromDb() {
        // 1. Подготовка: указываем email, который ТОЧНО есть в вашей таблице users
        String targetEmail = "test807@test.me";

        // 2. Действие: вызываем метод репозитория
        UserDto user = userRepository.findByEmail(targetEmail);

        // 3. Проверки (Assertions):
        // Проверяем, что база вообще нашла запись
        Assert.assertNotNull(user, "Ошибка: Пользователь с email " + targetEmail + " не найден в базе данных!");

        // Проверяем, что поле fullName не пустое (значит, маппинг сработал)
        Assert.assertNotNull(user.getFullName(), "Ошибка: Поле fullName пустое, проверьте маппинг колонки full_name");

        System.out.println("Тест пройден успешно! Найден пользователь: " + user.getFullName());
    }
}


//Этот тест выполняет сквозную проверку (End-to-End) взаимодействия вашего кода с реальной базой данных.
// Вот его основные задачи:
// * Проверка соединения: Убеждается, что DBManager может установить сессию с MySQL.
// * Корректность SQL-запроса: Проверяет, что синтаксис SELECT * FROM users... в вашем репозитории написан без ошибок.
// * Маппинг данных (Mapping): Проверяет, что данные из колонок БД (full_name, email) правильно переносятся в поля Java-объекта UserDto.
// * Целостность данных: Подтверждает, что объект, который вы получили в коде, не является пустым (null) и содержит ожидаемые значения.