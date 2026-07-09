package com.streetlight.ui;

import com.streetlight.model.SimulationResult;
import com.streetlight.model.StreetLightSimulation;
import com.streetlight.service.HolidayService;
import com.streetlight.util.DateUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SuppressWarnings({"serial", "this-escape"})
public class StreetLightGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final HolidayService holidayService = new HolidayService();
    private final StreetLightSimulation simulation = new StreetLightSimulation(holidayService);
    private LocalDate selectedDate = LocalDate.now();

    private final JPanel metricCardsPanel = new JPanel(new GridLayout(1, 5, 14, 0));
    private JLabel clockLabel;
    private JLabel headerDateLabel;
    private JLabel selectedDateValue;
    private JLabel dayNameValue;
    private JLabel holidayStatusValue;
    private JLabel currentPatternValue;
    private JLabel patternShiftCountValue;
    private JLabel lightsOnValue;
    private JLabel lightsOffValue;
    private JLabel energySavedValue;
    private JLabel systemStatusValue;
    private JLabel prevHolidayValue;
    private JLabel nextHolidayValue;
    private JLabel daysRemainingValue;
    private JLabel nextHolidayNameValue;
    private JLabel statusBar;
    private JPanel lightGridPanel;
    private CalendarPanel calendarPanel;
    private JPopupMenu datePopup;

    public StreetLightGUI() {
        setTitle("Smart Street Light Automation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 860);
        setMinimumSize(new Dimension(1180, 760));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND);

        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UIConstants.BACKGROUND);
        main.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(main);

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(createContentPanel(), BorderLayout.CENTER);
        main.add(createStatusBar(), BorderLayout.SOUTH);

        refreshForDate(selectedDate);
        startClock();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StreetLightGUI::new);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 12));
        header.setOpaque(false);

        JLabel title = UIFactory.createLabel("Smart Street Light Automation System", UIConstants.TITLE_FONT, UIConstants.HEADER_TEXT);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        statusPanel.setOpaque(false);

        RoundedButton dateButton = UIFactory.createActionButton("📅 Select Date", UIConstants.PRIMARY, UIConstants.PRIMARY_HOVER);
        dateButton.addActionListener(e -> showDatePicker(dateButton));

        headerDateLabel = UIFactory.createLabel("", UIConstants.SMALL_FONT, UIConstants.HEADER_TEXT);
        clockLabel = UIFactory.createLabel("", UIConstants.SMALL_FONT, UIConstants.HEADER_TEXT);

        JPanel clockPanel = new JPanel();
        clockPanel.setOpaque(false);
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.add(headerDateLabel);
        clockPanel.add(clockLabel);

        statusPanel.add(dateButton);
        statusPanel.add(clockPanel);
        header.add(titlePanel, BorderLayout.WEST);
        header.add(statusPanel, BorderLayout.EAST);
        header.setBackground(UIConstants.HEADER_BACKGROUND);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        return header;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);

        metricCardsPanel.setOpaque(false);
        content.add(metricCardsPanel, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 16, 16));
        body.setOpaque(false);
        body.add(createLeftPanel());
        body.add(createCalendarContainer());

        content.add(body, BorderLayout.CENTER);
        content.add(createInsightsPanel(), BorderLayout.SOUTH);
        return content;
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        RoundedPanel overview = UIFactory.createCard("Operations Overview");
        overview.add(createOverviewPanel(), BorderLayout.CENTER);

        RoundedPanel mapCard = UIFactory.createCard("Street Light Pole Map");
        mapCard.add(createLightStatusPanel(), BorderLayout.CENTER);
        mapCard.setPreferredSize(new Dimension(0, 260));

        left.add(overview);
        left.add(Box.createRigidArea(new Dimension(0, 16)));
        left.add(mapCard);
        return left;
    }

    private JPanel createOverviewPanel() {
        JPanel overview = new JPanel(new BorderLayout(12, 12));
        overview.setOpaque(false);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        toolbar.setOpaque(false);

        RoundedButton simulateButton = UIFactory.createActionButton("▶ Run Simulation", UIConstants.PRIMARY, UIConstants.PRIMARY_HOVER);
        simulateButton.setPreferredSize(UIConstants.BUTTON_SIZE);
        simulateButton.addActionListener(e -> refreshForDate(selectedDate));

        RoundedButton resetButton = UIFactory.createActionButton("↺ Reset", UIConstants.SECONDARY, UIConstants.SECONDARY_HOVER);
        resetButton.setPreferredSize(UIConstants.SMALL_BUTTON_SIZE);
        resetButton.addActionListener(e -> resetSystem());

        toolbar.add(simulateButton);
        toolbar.add(resetButton);

        overview.add(toolbar, BorderLayout.NORTH);
        overview.add(createInfoGrid(), BorderLayout.CENTER);
        return overview;
    }

    private JPanel createInfoGrid() {
        JPanel grid = new JPanel(new GridLayout(5, 2, 10, 10));
        grid.setOpaque(false);

        selectedDateValue = UIFactory.createValueLabel("--");
        dayNameValue = UIFactory.createValueLabel("--");
        holidayStatusValue = UIFactory.createValueLabel("--");
        currentPatternValue = UIFactory.createValueLabel("--");
        patternShiftCountValue = UIFactory.createValueLabel("--");
        lightsOnValue = UIFactory.createValueLabel("--");
        lightsOffValue = UIFactory.createValueLabel("--");
        energySavedValue = UIFactory.createValueLabel("--");
        systemStatusValue = UIFactory.createValueLabel("--");

        grid.add(UIFactory.createInfoRow("Selected Date", selectedDateValue));
        grid.add(UIFactory.createInfoRow("Day Name", dayNameValue));
        grid.add(UIFactory.createInfoRow("Government Holiday", holidayStatusValue));
        grid.add(UIFactory.createInfoRow("Current Pattern", currentPatternValue));
        grid.add(UIFactory.createInfoRow("Pattern Shift Count", patternShiftCountValue));
        grid.add(UIFactory.createInfoRow("Lights ON", lightsOnValue));
        grid.add(UIFactory.createInfoRow("Lights OFF", lightsOffValue));
        grid.add(UIFactory.createInfoRow("Energy Saved", energySavedValue));
        grid.add(UIFactory.createInfoRow("System Status", systemStatusValue));

        return grid;
    }

    private JPanel createLightStatusPanel() {
        lightGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        lightGridPanel.setOpaque(false);
        return lightGridPanel;
    }

    private JPanel createCalendarContainer() {
        RoundedPanel calendarCard = UIFactory.createCard("Monthly Calendar");
        calendarCard.setLayout(new BorderLayout(12, 12));
        calendarPanel = new CalendarPanel(holidayService, this::handleDateSelection);
        calendarPanel.setSelectedDate(selectedDate);
        calendarCard.add(calendarPanel, BorderLayout.CENTER);
        calendarCard.setPreferredSize(new Dimension(0, 0));
        return calendarCard;
    }

    private JPanel createInsightsPanel() {
        RoundedPanel insights = UIFactory.createCard("Holiday Insight Cards");
        insights.setLayout(new GridLayout(1, 3, 14, 0));
        insights.setPreferredSize(new Dimension(0, 140));

        prevHolidayValue = UIFactory.createValueLabel("--");
        nextHolidayValue = UIFactory.createValueLabel("--");
        daysRemainingValue = UIFactory.createValueLabel("--");
        nextHolidayNameValue = UIFactory.createStatusText("--");

        insights.add(createInsightCard("Previous Holiday", prevHolidayValue, UIFactory.createStatusText("")));
        insights.add(createInsightCard("Next Holiday", nextHolidayValue, nextHolidayNameValue));
        insights.add(createInsightCard("Countdown", daysRemainingValue, UIFactory.createStatusText("")));

        return insights;
    }

    private JPanel createInsightCard(String title, JLabel valueLabel, JLabel extraLabel) {
        JPanel card = new RoundedPanel(16);
        card.setBackground(UIConstants.CARD_BACKGROUND);
        card.setLayout(new BorderLayout(6, 6));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));
        card.add(UIFactory.createLabel(title, UIConstants.LABEL_FONT, UIConstants.INFO_TEXT), BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(extraLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 14, 10, 14));
        statusBar = UIFactory.createStatusText("System ready");
        bar.add(statusBar, BorderLayout.WEST);
        return bar;
    }

    private void refreshForDate(LocalDate date) {
        selectedDate = DateUtilities.clampToSupportedRange(date);
        SimulationResult result = simulation.simulate(selectedDate);
        boolean isHoliday = result.isHoliday();

        selectedDateValue.setText(DateUtilities.formatDate(selectedDate));
        dayNameValue.setText(DateUtilities.formatDayName(selectedDate));
        holidayStatusValue.setText(isHoliday ? "Yes" : "No");
        currentPatternValue.setText(result.getPatternLabel());
        patternShiftCountValue.setText(String.valueOf(result.getPatternShiftCount()));
        lightsOnValue.setText(String.valueOf(result.getActiveCount()));
        lightsOffValue.setText(String.valueOf(result.getInactiveCount()));
        energySavedValue.setText(Math.round((result.getInactiveCount() / 7.0) * 100) + "%");
        systemStatusValue.setText(result.getStatusText());
        updateMetricCards(result, isHoliday);
        updatePoleMap(result);
        updateHolidayInsights(selectedDate);
        statusBar.setText(isHoliday ? "Government holiday mode enabled" : "Simulation completed successfully");
        if (calendarPanel != null) {
            calendarPanel.setSelectedDate(selectedDate);
        }
    }

    private void updateMetricCards(SimulationResult result, boolean isHoliday) {
        metricCardsPanel.removeAll();
        metricCardsPanel.add(UIFactory.createMetricCard("Active Poles", String.valueOf(result.getActiveCount()), "Operational", UIConstants.SUCCESS));
        metricCardsPanel.add(UIFactory.createMetricCard("Inactive Poles", String.valueOf(result.getInactiveCount()), "Offline", UIConstants.ERROR));
        metricCardsPanel.add(UIFactory.createMetricCard("Energy Saved", Math.round((result.getInactiveCount() / 7.0) * 100) + "%", "Efficiency", UIConstants.ACCENT));
        metricCardsPanel.add(UIFactory.createMetricCard("Holiday", isHoliday ? "Yes" : "No", isHoliday ? "Government" : "Normal Day", UIConstants.WARNING));
        metricCardsPanel.add(UIFactory.createMetricCard("Pattern", result.getPatternLabel(), "Lighting Strategy", UIConstants.PRIMARY));
        metricCardsPanel.revalidate();
        metricCardsPanel.repaint();
    }

    private void updatePoleMap(SimulationResult result) {
        lightGridPanel.removeAll();
        boolean[] states = result.getPoleStates();
        for (int index = 0; index < states.length; index++) {
            lightGridPanel.add(new LightIndicator(index + 1, states[index]));
        }
        lightGridPanel.revalidate();
        lightGridPanel.repaint();
    }

    private void updateHolidayInsights(LocalDate date) {
        LocalDate previousHoliday = holidayService.getPreviousHoliday(date);
        LocalDate nextHoliday = holidayService.getNextHoliday(date);
        prevHolidayValue.setText(previousHoliday == null ? "None" : DateUtilities.formatDate(previousHoliday));
        nextHolidayValue.setText(nextHoliday == null ? "None" : DateUtilities.formatDate(nextHoliday));
        nextHolidayNameValue.setText(nextHoliday == null ? "None" : holidayService.getHolidayName(nextHoliday));
        daysRemainingValue.setText(nextHoliday == null ? "--" : ChronoUnit.DAYS.between(date, nextHoliday) + " day(s)");
    }

    private void resetSystem() {
        simulation.reset();
        selectedDate = LocalDate.now();
        refreshForDate(selectedDate);
        statusBar.setText("System reset successfully");
    }

    private void showDatePicker(Component anchor) {
        if (datePopup == null) {
            datePopup = new JPopupMenu();
            datePopup.setBorder(new EmptyBorder(8, 8, 8, 8));
            CalendarPanel popupCalendar = new CalendarPanel(holidayService, date -> {
                handleDateSelection(date);
                datePopup.setVisible(false);
            });
            popupCalendar.setSelectedDate(selectedDate);
            datePopup.add(popupCalendar);
        }
        datePopup.show(anchor, 0, anchor.getHeight());
    }

    private void handleDateSelection(LocalDate date) {
        selectedDate = date;
        refreshForDate(date);
    }

    private void startClock() {
        Timer clockTimer = new Timer(1000, e -> refreshHeaderClock());
        clockTimer.start();
        refreshHeaderClock();
    }

    private void refreshHeaderClock() {
        clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        headerDateLabel.setText(DateUtilities.formatDate(LocalDate.now()));
    }
}
