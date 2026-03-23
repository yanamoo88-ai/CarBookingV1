package com.carbooking.taskApp.database.tests;


import com.carbooking.database.repository.CarTypeRepository;
import com.carbooking.dto.CarTypeDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CarTypeDatabaseTest {

    public CarTypeRepository carTypeRepository = new CarTypeRepository();

    @Test
    public void testGetCarTypeByIdFromDb() {
        Integer targetId = 3;

        CarTypeDto carType = carTypeRepository.findById(targetId);

        Assert.assertNotNull(carType, "Car type not found!");
        Assert.assertNotNull(carType.getName(), "Car type not found!");

        System.out.println("Car type: " + carType.getName() + ", Price per day: " + carType.getPricePerDay());
    }
}
