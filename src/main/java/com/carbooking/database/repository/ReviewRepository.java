package com.carbooking.database.repository;

import com.carbooking.database.DBManager;
import com.carbooking.dto.ReviewDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {

    public ReviewDto findById(Integer id) {
        String sql = "SELECT id,user_id,rating,content FROM reviews WHERE id = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return ReviewDto.builder()
                        .id(resultSet.getInt("id"))
                    //    .bookingId(resultSet.getInt("car_id"))
                        .userId(resultSet.getInt("user_id")) // Читаем новое поле
                        .rating(resultSet.getInt("rating"))
                        .content(resultSet.getString("content"))
                       // .createdAt(resultSet.getString("created_at"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске отзыва: " + e.getMessage());
        }
        return null;
    }
    // Метод для получения всех отзывов
    public List<ReviewDto> findAll() {
        List<ReviewDto> reviews = new ArrayList<>();
        String sql = "SELECT id, user_id,rating, content FROM reviews";

        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(ReviewDto.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                    //    .bookingId(rs.getInt("car_id"))
                        .rating(rs.getInt("rating"))
                        .content(rs.getString("content"))
                        .build());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка отзывов: " + e.getMessage());
        }
        return reviews;
    }
}


