package com.streetlight.ui;

import com.streetlight.service.HolidayService;
import com.streetlight.util.DateUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;

@SuppressWarnings({"serial", "this-escape"})
public class CalendarPanel extends JPanel {
    private final HolidayService holidayService;
    private final Consumer<LocalDate> selectionHandler;
    private YearMonth displayedYearMonth;
    private LocalDate selectedDate;
    private final JPanel gridPanel = new JPanel(new GridLayout(0, 7, 6, 6));
    private final JComboBox<String> monthCombo = new JComboBox<>(DateUtilities.MONTH_NAMES);
    private final JComboBox<Integer> yearCombo = new JComboBox<>();

    public CalendarPanel(HolidayService holidayService, Consumer<LocalDate> selectionHandler) {
        this.holidayService = holidayService;
        this.selectionHandler = selectionHandler;
        this.selectedDate = LocalDate.now();
        this.displayedYearMonth = YearMonth.from(selectedDate);

        initializeYearOptions();
        buildUI();
        updateCalendar();
    }

    private void initializeYearOptions() {
        for (int year = DateUtilities.MIN_YEAR; year <= DateUtilities.MAX_YEAR; year++) {
            yearCombo.addItem(year);
        }
    }

    private void buildUI() {
        setLayout(new BorderLayout(12, 12));
        setOpaque(false);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topBar = new RoundedPanel(18);
        topBar.setBackground(UIConstants.CARD_BACKGROUND);
        topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));

        JButton prevMonth = createNavigationButton("←");
        JButton nextMonth = createNavigationButton("→");
        monthCombo.setFont(UIConstants.NORMAL_FONT);
        yearCombo.setFont(UIConstants.NORMAL_FONT);
        monthCombo.setBackground(UIConstants.BACKGROUND);
        yearCombo.setBackground(UIConstants.BACKGROUND);
        monthCombo.setOpaque(true);
        yearCombo.setOpaque(true);
        monthCombo.addActionListener(e -> updateDisplayMonth());
        yearCombo.addActionListener(e -> updateDisplayYear());

        prevMonth.addActionListener(e -> changeMonth(-1));
        nextMonth.addActionListener(e -> changeMonth(1));

        topBar.add(prevMonth);
        topBar.add(monthCombo);
        topBar.add(yearCombo);
        topBar.add(nextMonth);

        add(topBar, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = DateUtilities.clampToSupportedRange(date);
        this.displayedYearMonth = YearMonth.from(this.selectedDate);
        monthCombo.setSelectedIndex(displayedYearMonth.getMonthValue() - 1);
        yearCombo.setSelectedItem(displayedYearMonth.getYear());
        updateCalendar();
    }

    private void changeMonth(int offset) {
        displayedYearMonth = displayedYearMonth.plusMonths(offset);
        if (displayedYearMonth.getYear() < DateUtilities.MIN_YEAR) {
            displayedYearMonth = YearMonth.of(DateUtilities.MIN_YEAR, displayedYearMonth.getMonthValue());
        }
        if (displayedYearMonth.getYear() > DateUtilities.MAX_YEAR) {
            displayedYearMonth = YearMonth.of(DateUtilities.MAX_YEAR, displayedYearMonth.getMonthValue());
        }
        syncSelectionFromDisplayedMonth();
        updateCalendar();
    }

    private void updateDisplayMonth() {
        displayedYearMonth = YearMonth.of(displayedYearMonth.getYear(), monthCombo.getSelectedIndex() + 1);
        syncSelectionFromDisplayedMonth();
        updateCalendar();
    }

    private void updateDisplayYear() {
        displayedYearMonth = YearMonth.of((Integer) yearCombo.getSelectedItem(), displayedYearMonth.getMonthValue());
        syncSelectionFromDisplayedMonth();
        updateCalendar();
    }

    private void syncSelectionFromDisplayedMonth() {
        int day = Math.min(selectedDate.getDayOfMonth(), displayedYearMonth.lengthOfMonth());
        selectedDate = LocalDate.of(displayedYearMonth.getYear(), displayedYearMonth.getMonthValue(), day);
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.SECTION_FONT);
        button.setPreferredSize(new Dimension(42, 34));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBackground(UIConstants.PRIMARY);
        button.setForeground(UIConstants.HEADER_TEXT);
        return button;
    }

    private void updateCalendar() {
        gridPanel.removeAll();
        monthCombo.setSelectedIndex(displayedYearMonth.getMonthValue() - 1);
        yearCombo.setSelectedItem(displayedYearMonth.getYear());

        String[] weekdays = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        for (String weekday : weekdays) {
            JLabel header = UIFactory.createLabel(weekday, UIConstants.LABEL_FONT, UIConstants.PRIMARY);
            header.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(header);
        }

        LocalDate firstOfMonth = displayedYearMonth.atDay(1);
        int offset = firstOfMonth.getDayOfWeek().getValue() - 1;
        for (int i = 0; i < offset; i++) {
            gridPanel.add(new JLabel());
        }

        LocalDate today = LocalDate.now();
        for (int day = 1; day <= displayedYearMonth.lengthOfMonth(); day++) {
            LocalDate date = displayedYearMonth.atDay(day);
            JButton cell = new JButton(String.valueOf(day));
            cell.setFont(UIConstants.NORMAL_FONT);
            cell.setFocusPainted(false);
            cell.setOpaque(true);
            cell.setBorder(new LineBorder(UIConstants.BORDER, 1));
            cell.setPreferredSize(new Dimension(42, 38));

            boolean isHoliday = holidayService.isGovernmentHoliday(date);
            boolean isToday = date.equals(today);
            boolean isSelected = date.equals(selectedDate);
            Color background = isHoliday ? UIConstants.ERROR : UIConstants.BACKGROUND;
            Color foreground = UIConstants.TEXT;

            if (isSelected) {
                background = UIConstants.PRIMARY;
                foreground = UIConstants.HEADER_TEXT;
                cell.setBorder(new LineBorder(UIConstants.SECONDARY, 2));
            } else if (isToday) {
                background = UIConstants.ACCENT;
                foreground = UIConstants.HEADER_TEXT;
            } else if (!isHoliday) {
                background = day % 2 == 0 ? UIConstants.BACKGROUND : UIConstants.CARD_BACKGROUND;
            }

            cell.setBackground(background);
            cell.setForeground(foreground);
            cell.addActionListener(e -> {
                selectedDate = date;
                selectionHandler.accept(date);
                updateCalendar();
            });
            gridPanel.add(cell);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
