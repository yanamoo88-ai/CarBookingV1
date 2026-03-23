package com.carbooking.taskApp.database.tests;

import com.carbooking.database.repository.CarRepository;
import com.carbooking.dto.CarDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CarDatabaseTest {

    CarRepository carRepository = new CarRepository();

    @Test
    public void testGetCarByIdFromDb() {

        Integer targetId = 3;

        CarDto car = carRepository.findById(targetId);

                Assert.assertNotNull(car, "Car with ID " + targetId + " not found in the database!");
        Assert.assertNotNull(car.getModel(), "The model field must not be empty");

        System.out.println("Test passed.Car found: " + car.getModel() + " (status: " + car.getStatus() + ")");
    }
}
