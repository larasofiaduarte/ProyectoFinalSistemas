package com.mycompany.GUI.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class MinimalScrollBarUI extends BasicScrollBarUI {

    private static final int THUMB_WIDTH = 6;
    private static final Color THUMB_COLOR = new Color(160, 160, 160, 180);
    private static final Color THUMB_HOVER_COLOR = new Color(120, 120, 120, 220);

    private boolean hovered = false;

    @Override
    protected void installListeners() {
        super.installListeners();
        scrollbar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                scrollbar.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                scrollbar.repaint();
            }
        });
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return zeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return zeroButton();
    }

    private JButton zeroButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // intentionally empty — no visible track
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hovered ? THUMB_HOVER_COLOR : THUMB_COLOR);

        int arc = THUMB_WIDTH;
        int x = thumbBounds.x + (thumbBounds.width - THUMB_WIDTH) / 2;
        int y = thumbBounds.y + 2;
        int w = THUMB_WIDTH;
        int h = thumbBounds.height - 4;

        g2.fillRoundRect(x, y, w, h, arc, arc);
        g2.dispose();
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(THUMB_WIDTH, 30);
    }
}
