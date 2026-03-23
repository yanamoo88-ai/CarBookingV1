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
                String targetEmail = "test807@test.me";

            UserDto user = userRepository.findByEmail(targetEmail);

               Assert.assertNotNull(user, "Error: User with email " + targetEmail + " not found in the database!");

        // Проверяем, что поле fullName не пустое (значит, маппинг сработал)
        Assert.assertNotNull(user.getFull_name(), "Error: The full_name field is empty");

        System.out.println("Test completed successfully! User found:" + user.getFull_name());
    }
}

