package com.mycompany.GUI.components;

import javax.swing.*;
import java.awt.*;

public class ThemeToggle extends JToggleButton {

    private float anim = 0f; // 0 = dark (left), 1 = light (right)
    private final Timer timer;

    private final Image moonIcon;
    private final Image sunIcon;

    public ThemeToggle() {
        setPreferredSize(new Dimension(90, 32));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        moonIcon = new ImageIcon(
                getClass().getResource("/images/moon.png")
        ).getImage();

        sunIcon = new ImageIcon(
                getClass().getResource("/images/sun.png")
        ).getImage();

        timer = new Timer(15, e -> animate());
        addActionListener(e -> timer.start());
    }

    private void animate() {
        if (isSelected()) {
            anim += 0.08f;
            if (anim >= 1f) {
                anim = 1f;
                timer.stop();
            }
        } else {
            anim -= 0.08f;
            if (anim <= 0f) {
                anim = 0f;
                timer.stop();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // ===== background =====
        Color darkBg  = new Color(70, 70, 70);
        Color lightBg = new Color(127, 52, 201);

        g2.setColor(blend(darkBg, lightBg, anim));
        g2.fillRoundRect(0, 0, w, h, h, h);

        // ===== icons (SWAPPED) =====
        int iconSize = 16;
        int padding = 8;
        int iconY = (h - iconSize) / 2;

        int sunX  = padding;                         // LEFT
        int moonX = w - iconSize - padding;          // RIGHT

        // sun fades out when going to dark
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, anim));
        g2.drawImage(moonIcon, sunX, iconY, iconSize, iconSize, null);

        // moon fades in when going to light
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1f - anim));
        g2.drawImage(sunIcon, moonX, iconY, iconSize, iconSize, null);

        g2.setComposite(AlphaComposite.SrcOver);

        // ===== knob =====
        int knobSize = h - 6;
        int knobX = (int) (3 + anim * (w - knobSize - 6));

        g2.setColor(Color.WHITE);
        g2.fillOval(knobX, 3, knobSize, knobSize);

        g2.dispose();
    }

    private Color blend(Color c1, Color c2, float t) {
        int r = (int) (c1.getRed()   + t * (c2.getRed()   - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue()  + t * (c2.getBlue()  - c1.getBlue()));
        return new Color(r, g, b);
    }
}
