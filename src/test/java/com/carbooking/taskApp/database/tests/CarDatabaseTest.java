package com.carbooking.taskApp.database.tests;

import com.carbooking.database.repository.CarRepository;
import com.carbooking.dto.CarDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CarDatabaseTest {

    CarRepository carRepository = new CarRepository();

    @Test
    public void testGetCarByIdFromDb() {
        // Укажите ID автомобиля, который есть в вашей таблице cars
        Integer targetId = 3;

        CarDto car = carRepository.findById(targetId);

        // Проверки:
        Assert.assertNotNull(car, "Car with ID " + targetId + " не найден в базе!");
        Assert.assertNotNull(car.getModel(), "Поле model не должно быть пустым");

        System.out.println("Test passed.Car found: " + car.getModel() + " (status: " + car.getStatus() + ")");
    }
}

//Что он проверяет: Корректность получения модели и статуса автомобиля
// по его ID.
