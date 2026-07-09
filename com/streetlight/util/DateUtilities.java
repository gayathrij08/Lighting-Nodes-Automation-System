package com.streetlight.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class DateUtilities {
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    public static final int MIN_YEAR = CURRENT_YEAR - 50;
    public static final int MAX_YEAR = CURRENT_YEAR + 50;
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    public static final String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private DateUtilities() {
    }

    public static int[] getSupportedYears() {
        int[] years = new int[MAX_YEAR - MIN_YEAR + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = MIN_YEAR + i;
        }
        return years;
    }

    public static int getMaxDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public static String formatDate(LocalDate date) {
        return date == null ? "--" : date.format(DISPLAY_FORMATTER);
    }

    public static String formatDayName(LocalDate date) {
        return date == null ? "--" : capitalize(date.getDayOfWeek().toString());
    }

    public static LocalDate clampToSupportedRange(LocalDate date) {
        if (date == null) {
            return LocalDate.of(MIN_YEAR, 1, 1);
        }
        if (date.getYear() < MIN_YEAR) {
            return LocalDate.of(MIN_YEAR, date.getMonthValue(), Math.min(date.getDayOfMonth(), getMaxDaysInMonth(MIN_YEAR, date.getMonthValue())));
        }
        if (date.getYear() > MAX_YEAR) {
            return LocalDate.of(MAX_YEAR, date.getMonthValue(), Math.min(date.getDayOfMonth(), getMaxDaysInMonth(MAX_YEAR, date.getMonthValue())));
        }
        return date;
    }

    private static String capitalize(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
