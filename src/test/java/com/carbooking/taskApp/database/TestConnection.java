package com.carbooking.taskApp.database;
import com.carbooking.database.DBManager;
import com.carbooking.database.repository.CarRepository;
import com.carbooking.dto.CarDto;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.sql.Connection;
import java.util.List;

public class TestConnection {

    @Test
    public void testDBConnection() {
        Connection conn = DBManager.getConnection();
        assert conn != null;
        System.out.println("DB connection OK");
    }
    @Test
    public void testGetAllCarsFromDb() {
        // 1. Создаем экземпляр репозитория
        CarRepository carRepository = new CarRepository();

        // 2. Вызываем метод получения всех машин (он использует твой CarDto и Builder)
        List<CarDto> cars = carRepository.findAll();

        // 3. Проверяем, что список не пустой
        Assert.assertFalse(cars.isEmpty(), "Список машин в базе данных пуст!");

        // 4. Проверяем количество (на сайте их 6, проверим и тут)
        Assert.assertEquals(cars.size(), 6, "Количество машин в базе не совпадает с ожидаемым!");

        // 5. Выведем в консоль первую машину, чтобы убедиться в правильности полей
        //System.out.println("Проверка первой машины: " + cars.get(0).getBrand() + " " + cars.get(0).getModel());
        System.out.println("Всего машин в базе: " + cars.size());
        for (CarDto car : cars) {
            System.out.println("Car: " + car.getBrand() + " " + car.getModel() + " | Status: " + car.getStatus());
        }
    }
}