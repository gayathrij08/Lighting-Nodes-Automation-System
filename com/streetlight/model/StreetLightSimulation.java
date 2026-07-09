package com.streetlight.model;

import com.streetlight.service.HolidayService;

import java.time.LocalDate;

public class StreetLightSimulation {
    private final HolidayService holidayService;
    private final PatternController patternController;

    public StreetLightSimulation(HolidayService holidayService) {
        this.holidayService = holidayService;
        this.patternController = new PatternController();
    }

    public SimulationResult simulate(LocalDate date) {
        return patternController.evaluate(date, holidayService.isGovernmentHoliday(date));
    }

    public void reset() {
        patternController.reset();
    }

    public int getPatternShiftCount() {
        return patternController.getPatternShiftCount();
    }
}
