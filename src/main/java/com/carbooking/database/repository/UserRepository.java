package com.carbooking.database.repository;

import com.carbooking.database.DBManager;
import com.carbooking.dto.UserDto;
import java.sql.*;

public class UserRepository {
    public UserDto findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return UserDto.builder()
                        .id(resultSet.getInt("id"))
                        .fullName(resultSet.getString("full_name")) // Читаем full_name из БД
                        .email(resultSet.getString("email"))
                        .phone(resultSet.getString("phone"))
                        .role(resultSet.getString("role"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователя: " + e.getMessage());
        }
        return null;
    }
}
