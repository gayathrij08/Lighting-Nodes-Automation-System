package com.streetlight.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HolidayService {
    private static final int MIN_YEAR = LocalDate.now().getYear() - 50;
    private static final int MAX_YEAR = LocalDate.now().getYear() + 50;
    private static final Pattern DATE_PATTERN = Pattern.compile("\"date\"\\s*:\\s*\"(\\d{4}-\\d{2}-\\d{2})\"");

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean isGovernmentHoliday(LocalDate date) {
        if (date == null || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }
        return getHolidays(date.getYear()).contains(date);
    }

    public LocalDate getPreviousHoliday(LocalDate date) {
        if (date == null) {
            return null;
        }
        List<LocalDate> candidates = new ArrayList<>();
        for (int year = MIN_YEAR; year <= MAX_YEAR; year++) {
            candidates.addAll(getHolidays(year));
        }
        return candidates.stream()
                .filter(holiday -> !holiday.isAfter(date))
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public LocalDate getNextHoliday(LocalDate date) {
        if (date == null) {
            return null;
        }
        List<LocalDate> candidates = new ArrayList<>();
        for (int year = MIN_YEAR; year <= MAX_YEAR; year++) {
            candidates.addAll(getHolidays(year));
        }
        return candidates.stream()
                .filter(holiday -> holiday.isAfter(date))
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    public List<LocalDate> getHolidays(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return new ArrayList<>();
        }

        List<LocalDate> holidays = new ArrayList<>();
        try {
            holidays = loadFromApi(year);
        } catch (Exception ignored) {
            holidays = buildFallbackHolidays(year);
        }
        holidays.removeIf(holiday -> holiday == null || holiday.getDayOfWeek() == DayOfWeek.SUNDAY);
        holidays.sort(Comparator.naturalOrder());
        return holidays;
    }

    public String getHolidayName(LocalDate date) {
        if (date == null || !isGovernmentHoliday(date)) {
            return "None";
        }
        return "Government Holiday";
    }

    private List<LocalDate> loadFromApi(int year) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://date.nager.at/api/v3/PublicHolidays/" + year + "/IN"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Holiday API unavailable");
        }

        List<LocalDate> holidays = new ArrayList<>();
        Matcher matcher = DATE_PATTERN.matcher(response.body());
        while (matcher.find()) {
            holidays.add(LocalDate.parse(matcher.group(1)));
        }
        return holidays;
    }

    private List<LocalDate> buildFallbackHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();
        addHoliday(holidays, LocalDate.of(year, 1, 1));
        addHoliday(holidays, LocalDate.of(year, 1, 26));
        addHoliday(holidays, LocalDate.of(year, 5, 1));
        addHoliday(holidays, LocalDate.of(year, 8, 15));
        addHoliday(holidays, LocalDate.of(year, 10, 2));
        addHoliday(holidays, LocalDate.of(year, 12, 25));
        addHoliday(holidays, LocalDate.of(year, 9, 5));
        return holidays;
    }

    private void addHoliday(List<LocalDate> holidays, LocalDate date) {
        if (date != null && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            holidays.add(date);
        }
    }
}
