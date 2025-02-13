package com.attendance.Utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static LocalDate sqlDateToLocalDate(java.sql.Date date) {
        // Convert java.sql.Date to java.util.Date and then to Instant
        Instant instant = date.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Convert Instant to LocalDate
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();

    }


//    public static LocalDate utilDateToLocalDate(Date utilDate) {
//        return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    }



}
