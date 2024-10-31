package com.lanaVitor.Reservas.com.services.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UtilService {

    private static final double PRICE_PER_DAY = 100;

    public static String calculateTotalPrice(LocalDateTime checkIn, LocalDateTime checkOut) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String checkInForSending = checkIn.format(formatter);
        String checkOutForSending = checkOut.format(formatter);

        long daysBetween = calculateDaysBetween(checkIn, checkOut);
        double totalPrice = daysBetween * PRICE_PER_DAY;

        String sb = "------- Nota Fiscal -------\n" +
                "Data de check-in: " + checkInForSending + "\n" +
                "Data de check-out: " + checkOutForSending + "\n" +
                "Total a pagar: R$ " + totalPrice + "\n" +
                "----------------------------\n";
        return sb;
    }

    private static long calculateDaysBetween(LocalDateTime checkIn, LocalDateTime checkOut) {
        LocalDate localCheckIn = checkIn.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localCheckOut = checkOut.atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(localCheckIn, localCheckOut);
    }
}
