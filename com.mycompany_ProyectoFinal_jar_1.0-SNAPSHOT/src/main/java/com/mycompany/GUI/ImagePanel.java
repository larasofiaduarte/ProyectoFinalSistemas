/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author duart
 */
public class ImagePanel extends JPanel {
    private Image image;
    private double zoomFactor = 1.5; // >1 = zoom in

    public ImagePanel(Image image) {
        this.image = image;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // keep components like labels centered
    }

    public void setZoomFactor(double zoom) {
        this.zoomFactor = zoom;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // original image size
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);

            // scaled size with zoom
            int w = (int)(iw * zoomFactor);
            int h = (int)(ih * zoomFactor);

            // top-left corner: start drawing at (0,0)
            g2.drawImage(image, 0, 0, w, h, this);

            g2.dispose();
        }
    }
}
