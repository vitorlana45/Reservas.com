package com.lanaVitor.Reservas.com.services.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class UtilService {

    private static final double PRICE_PER_DAY = 85;

    public static String calculateTotalPrice(Date checkIn, Date checkOut) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String checkInForSending = sdf.format(checkIn);
        String checkOutForSending = sdf.format(checkOut);

        long daysBetween = calculateDaysBetween(checkIn, checkOut);
        double totalPrice = daysBetween * PRICE_PER_DAY;

        String sb = "------- Nota Fiscal -------\n" +
                "Data de check-in: " + checkInForSending + "\n" +
                "Data de check-out: " + checkOutForSending + "\n" +
                "Total a pagar: R$ " + totalPrice + "\n" +
                "----------------------------\n";
        return sb;
    }

    private static long calculateDaysBetween(Date checkIn, Date checkOut) {
        LocalDate localCheckIn = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localCheckOut = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(localCheckIn, localCheckOut);
    }
}
