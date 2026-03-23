package com.carbooking.utils;
import com.carbooking.dto.AuthRequestDto;
import com.carbooking.dto.UserRegistrationDto;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDataProvider {


    @DataProvider(name = "positiveRegistrationData")
    public Iterator<Object[]> positiveRegistrationData() throws IOException {
        List<Object[]> list = new ArrayList<>();
        //String path = "src/main/resources/data/positiveReg.csv";
        String path = "src/test/resources/data/positiveReg.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length >= 3) {
                    UserRegistrationDto user = UserRegistrationDto.builder()
                            .full_name(split[0].trim())
                            .email(split[1].trim())
                            //.phone(split[2].trim())
                            .password(split[2].trim())
                            .build();
                    list.add(new Object[]{user});
                }
            }
        }

        return list.iterator();
    }


@DataProvider(name = "negativeRegistrationData")
    public Iterator<Object[]> negativeRegistrationData() throws IOException {
        List<Object[]> list = new ArrayList<>();
        String path = "src/test/resources/data/negativeReg.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length >= 4) {
                    UserRegistrationDto user = UserRegistrationDto.builder()
                            .full_name(split[0].trim())
                            .email(split[1].trim())
                           // .phone(split[2].trim())
                            .password(split[2].trim())
                            .build();
                    String expectedError = split[3].trim();
                    list.add(new Object[]{user, expectedError});
                }
            }
        }

        return list.iterator();
    }
    @DataProvider(name = "invalidFilters")
    public static Object[][] getInvalidFilters() { // Добавь static
        return new Object[][]{
                {"/cars?type=123", "Числовой тип"},
                {"/cars?minPrice=abc", "Цена не число"}
        };
    }


    @DataProvider(name = "positiveAuth")
    public Iterator<Object[]> positiveAuth() throws IOException {
        return loadAuthData("src/test/resources/data/positiveAuth.csv");
    }

    @DataProvider(name = "negativeAuth")
    public Iterator<Object[]> negativeAuth() throws IOException {
        return loadAuthData("src/test/resources/data/negativeAuth.csv");
    }
    private Iterator<Object[]> loadAuthData(String path) throws IOException {
        List<Object[]> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (split.length >= 2) {
                    AuthRequestDto auth = AuthRequestDto.builder()
                            .email(split[0])
                            .password(split[1])
                            .build();
                    list.add(new Object[]{auth});
                }
            }
        }
        return list.iterator();
    }
    @DataProvider(name = "carsData")
    public Iterator<Object[]> carsData() throws IOException {
        List<Object[]> list = new ArrayList<>();
        // Путь к файлу с машинами (создай его по этому пути)
        String path = "src/test/resources/data/cars.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // Пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length >= 3) {
                    // Передаем бренд, модель и цену как массив объектов
                    list.add(new Object[]{
                            split[0].trim(), // brand
                            split[1].trim(), // model
                            split[2].trim() // price
                    });
                }
            }
        }
        return list.iterator();
    }
}