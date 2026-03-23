package com.carbooking.taskApp.database.tests;


import com.carbooking.database.repository.BookingRepository;
import com.carbooking.dto.BookingDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingDatabaseTest {

    BookingRepository bookingRepository = new BookingRepository();

    @Test
    public void testGetBookingByIdFromDb() {

        Integer targetId = 1;

        BookingDto booking = bookingRepository.findById(targetId);

               Assert.assertNotNull(booking, "Booking with ID" + targetId + " not found!");
        Assert.assertNotNull(booking.getUserId(), "User The booking ID must not be empty");

        System.out.println("Test passed! Booking found. Status: " + booking.getStatus());
    }
}