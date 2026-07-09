package com.streetlight.model;

import java.time.LocalDate;

public class PatternController {
    private boolean patternShift;
    private int patternShiftCount;

    public SimulationResult evaluate(LocalDate date, boolean isHoliday) {
        if (isHoliday) {
            patternShift = !patternShift;
            patternShiftCount++;
            boolean[] states = new boolean[7];
            return new SimulationResult(date, true, "Government Holiday", "Holiday - All Lights OFF", states, patternShiftCount);
        }

        boolean oddPattern = date.getDayOfMonth() % 2 != 0;
        if (patternShift) {
            oddPattern = !oddPattern;
        }

        boolean[] states = new boolean[7];
        for (int i = 0; i < 7; i++) {
            int pole = i + 1;
            states[i] = oddPattern ? pole % 2 != 0 : pole % 2 == 0;
        }

        return new SimulationResult(date, false, oddPattern ? "Odd Pattern" : "Even Pattern", "Simulation Complete", states, patternShiftCount);
    }

    public void reset() {
        patternShift = false;
        patternShiftCount = 0;
    }

    public int getPatternShiftCount() {
        return patternShiftCount;
    }
}
