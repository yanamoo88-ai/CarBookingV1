package com.carbooking.database.repository;

import com.carbooking.database.DBManager;
import com.carbooking.dto.BookingDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRepository {

    public BookingDto findById(Integer id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return BookingDto.builder()
                        .id(resultSet.getInt("id"))
                        .userId(resultSet.getInt("user_id"))
                        .carId(resultSet.getInt("car_id"))
                        .startDate(resultSet.getString("start_date"))
                        .endDate(resultSet.getString("end_date"))
                        .totalPrice(resultSet.getBigDecimal("total_price"))
                        .status(resultSet.getString("status"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Error searching for booking: " + e.getMessage());
        }
        return null;
    }
}
