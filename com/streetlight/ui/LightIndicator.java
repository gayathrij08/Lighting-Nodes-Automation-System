package com.streetlight.ui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("this-escape")
public class LightIndicator extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int poleNumber;
    private final boolean active;

    public LightIndicator(int poleNumber, boolean active) {
        this.poleNumber = poleNumber;
        this.active = active;
        setOpaque(false);
        setPreferredSize(UIConstants.LIGHT_INDICATOR_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int centerX = getWidth() / 2;
        g2.setColor(new Color(31, 41, 55));
        g2.fillRect(centerX - 4, 30, 8, 28);
        g2.setColor(active ? UIConstants.SUCCESS : UIConstants.ERROR);
        g2.fillOval(centerX - 16, 6, 32, 32);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(active ? new Color(187, 247, 208) : new Color(254, 202, 202));
        g2.drawOval(centerX - 16, 6, 32, 32);
        g2.setColor(UIConstants.TEXT);
        g2.setFont(UIConstants.LABEL_FONT);
        FontMetrics metrics = g2.getFontMetrics();
        String number = String.valueOf(poleNumber);
        int textWidth = metrics.stringWidth(number);
        g2.drawString(number, centerX - textWidth / 2, 58);
        g2.dispose();
    }
}
