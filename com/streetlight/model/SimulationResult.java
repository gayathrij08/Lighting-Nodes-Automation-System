package com.streetlight.model;

import java.time.LocalDate;

public class SimulationResult {
    private final LocalDate date;
    private final boolean holiday;
    private final String patternLabel;
    private final String statusText;
    private final boolean[] poleStates;
    private final int patternShiftCount;

    public SimulationResult(LocalDate date, boolean holiday, String patternLabel, String statusText, boolean[] poleStates, int patternShiftCount) {
        this.date = date;
        this.holiday = holiday;
        this.patternLabel = patternLabel;
        this.statusText = statusText;
        this.poleStates = poleStates;
        this.patternShiftCount = patternShiftCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public String getPatternLabel() {
        return patternLabel;
    }

    public String getStatusText() {
        return statusText;
    }

    public boolean[] getPoleStates() {
        return poleStates;
    }

    public int getPatternShiftCount() {
        return patternShiftCount;
    }

    public int getActiveCount() {
        int count = 0;
        for (boolean state : poleStates) {
            if (state) {
                count++;
            }
        }
        return count;
    }

    public int getInactiveCount() {
        return 7 - getActiveCount();
    }
}
