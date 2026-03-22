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

        Assert.assertNotNull(carType, "Тип автомобиля не найден!");
        Assert.assertNotNull(carType.getName(), "Название типа не может быть пустым");

        System.out.println("Тип авто: " + carType.getName() + ", Цена в день: " + carType.getPricePerDay());
    }
}
