package com.streetlight.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class UIFactory {
    private UIFactory() {
    }

    public static JLabel createLabel(String text, Font font, Color foreground) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(foreground);
        return label;
    }

    public static JLabel createValueLabel(String text) {
        JLabel label = createLabel(text, UIConstants.NORMAL_FONT, UIConstants.TEXT);
        label.setOpaque(false);
        return label;
    }

    public static RoundedButton createActionButton(String text, Color base, Color hover) {
        RoundedButton button = new RoundedButton(text, base, hover);
        button.setPreferredSize(UIConstants.BUTTON_SIZE);
        return button;
    }

    public static RoundedPanel createCard(String title) {
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(UIConstants.CARD_BACKGROUND);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel titleLabel = createLabel(title, UIConstants.SECTION_FONT, UIConstants.TEXT);
        card.add(titleLabel, BorderLayout.NORTH);
        return card;
    }

    public static JLabel createCardTitle(String text) {
        return createLabel(text, UIConstants.LABEL_FONT, UIConstants.TEXT);
    }

    public static JLabel createStatusText(String text) {
        return createLabel(text, UIConstants.NORMAL_FONT, UIConstants.INFO_TEXT);
    }

    public static JPanel createInfoRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel label = createLabel(labelText, UIConstants.LABEL_FONT, UIConstants.INFO_TEXT);
        row.add(label, BorderLayout.NORTH);
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    public static JPanel createMetricCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = new RoundedPanel(16);
        card.setBackground(UIConstants.CARD_BACKGROUND);
        card.setLayout(new BorderLayout(4, 4));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel titleLabel = createLabel(title, UIConstants.LABEL_FONT, UIConstants.INFO_TEXT);
        JLabel valueLabel = createLabel(value, UIConstants.METRIC_FONT, accentColor);
        JLabel subtitleLabel = createLabel(subtitle, UIConstants.NORMAL_FONT, UIConstants.INFO_TEXT);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);
        return card;
    }
}
