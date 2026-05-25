/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.*;
import javax.swing.*;

import java.awt.*;
import javax.swing.*;

public class ActiveNotif implements Icon {

    private Icon bell;
    private Icon dot;

    public ActiveNotif(Icon bell, Icon dot) {
        this.bell = bell;
        this.dot = dot;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // draw bell
        bell.paintIcon(c, g, x, y);

        // position dot (top-right corner)
        int offsetX = bell.getIconWidth() - dot.getIconWidth() / 2;
        int offsetY = 0;

        dot.paintIcon(c, g, x + offsetX, y );
    }

    @Override
    public int getIconWidth() {
        return bell.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return bell.getIconHeight();
    }
}