package com.carbooking.database.repository;

import com.carbooking.database.DBManager;
import com.carbooking.dto.CarDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {

    // Метод для получения ВСЕХ машин (используется в твоем новом тесте)
    public List<CarDto> findAll() {
        List<CarDto> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Connection conn = DBManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                // Строим объект CarDto из каждой строки базы данных
                CarDto car = CarDto.builder()
                        .id(resultSet.getInt("id"))
                        .brand(resultSet.getString("brand"))
                        .model(resultSet.getString("model"))
                        .numberPlate(resultSet.getString("number_plate")) // Исправлено на numberPlate
                        .categoryId(resultSet.getInt("category_id")) // Исправлено на categoryId
                        .status(resultSet.getString("status")) // Статус из БД мапим в available
                        .imageUrl(resultSet.getString("image_url"))
                        .build();

                // Добавляем машину в список (корзину)
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка в findAll: " + e.getMessage());
            e.printStackTrace();
        }
        return cars; // Возвращаем заполненный список
    }

    // Метод для поиска одной машины по ID (уже был у тебя в коде)
    public CarDto findById(Integer targetId) {
        String sql = "SELECT * FROM cars WHERE id = " + targetId;

        try (Connection conn = DBManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return CarDto.builder()
                        .id(resultSet.getInt("id"))
                        .brand(resultSet.getString("brand"))
                        .model(resultSet.getString("model"))
                        .numberPlate(resultSet.getString("number_plate"))
                        .categoryId(resultSet.getInt("category_id"))
                        .status(resultSet.getString("status"))
                        .imageUrl(resultSet.getString("image_url"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка в findById: " + e.getMessage());
        }
        return null;
    }
}
