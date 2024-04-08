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

        StringBuilder sb = new StringBuilder();
        sb.append("------- Nota Fiscal -------\n");
        sb.append("Data de check-in: ").append(checkInForSending).append("\n");
        sb.append("Data de check-out: ").append(checkOutForSending).append("\n");
        sb.append("Total a pagar: R$ ").append(totalPrice).append("\n");
        sb.append("----------------------------\n");
        return sb.toString();
    }

    private static long calculateDaysBetween(Date checkIn, Date checkOut) {
        LocalDate localCheckIn = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localCheckOut = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(localCheckIn, localCheckOut);
    }
}
