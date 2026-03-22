package com.carbooking.database.repository;

import com.carbooking.database.DBManager;
import com.carbooking.dto.CarTypeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarTypeRepository {
    public CarTypeDto findById(Integer id) {
        String sql = "SELECT * FROM car_categories WHERE id = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return CarTypeDto.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .pricePerDay(resultSet.getBigDecimal("price_per_day"))
                        .imageUrl(resultSet.getString("image_url"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске типа авто: " + e.getMessage());
        }
        return null;
    }
}


