package com.carbooking.taskApp.database;
import com.carbooking.database.DBManager;
import com.carbooking.database.repository.CarRepository;
import com.carbooking.dto.CarDto;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.sql.Connection;
import java.util.List;

public class TestConnection {
//
    @Test
    public void testDBConnection() {
        Connection conn = DBManager.getConnection();
        assert conn != null;
        System.out.println("DB connection OK");
    }
    @Test
    public void testGetAllCarsFromDb() {

        CarRepository carRepository = new CarRepository();


        List<CarDto> cars = carRepository.findAll();

                Assert.assertFalse(cars.isEmpty(), "The list of cars in the database is empty!");

        // 4. Проверяем количество (на сайте их 6, проверим и тут)
//        Assert.assertEquals(cars.size(), 6, "Количество машин в базе не совпадает с ожидаемым!");

        // 5. Выведем в консоль первую машину, чтобы убедиться в правильности полей
        //System.out.println("Проверка первой машины: " + cars.get(0).getBrand() + " " + cars.get(0).getModel());

        System.out.println("Total cars in the database: " + cars.size());
        for (CarDto car : cars) {
            System.out.println("Car: " + car.getBrand() + " " + car.getModel() + " | Status: " + car.getStatus());
        }
    }
}