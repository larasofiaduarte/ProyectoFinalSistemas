
package com.mycompany.GUI.cards;

import javax.swing.*;
import java.awt.*;
import com.mycompany.GUI.Styles;

import com.mycompany.GUI.components.*;

public abstract class MainPanelBase extends JPanel {

    protected TitlePanel titlePanel;
    protected JPanel topPanel;
    protected JPanel tablePanel;
    protected JPanel btnPanel;

    public MainPanelBase(String title) {
        initBaseUI(title);
    }

    private void initBaseUI(String title) {
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titlePanel = new TitlePanel(title);

        topPanel.add(titlePanel);
        add(topPanel, BorderLayout.NORTH);

        tablePanel = new JPanel(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);

        btnPanel = new JPanel();
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void applyTheme() {
        setBackground(Styles.bgLight);

        topPanel.setBackground(Styles.bgLight);
        tablePanel.setBackground(Styles.bgLight);
        btnPanel.setBackground(Styles.bgLight);

        if (titlePanel != null) {
            titlePanel.applyTheme();
        }

        repaint();
    }
}

