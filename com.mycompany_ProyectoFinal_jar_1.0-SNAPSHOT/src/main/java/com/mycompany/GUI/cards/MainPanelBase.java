
package com.mycompany.GUI.cards;

import javax.swing.*;
import java.awt.*;
import com.mycompany.GUI.Styles;
import java.util.List;

import com.mycompany.GUI.components.*;
import java.util.function.Function;

public abstract class MainPanelBase extends JPanel {

    protected TitlePanel titlePanel;
    protected JPanel topPanel;
    protected JPanel tablePanel;
    protected JPanel btnPanel;
    protected JPanel navPanel;
    
    protected Btn btnAlta;
    protected Btn btnEdit;
    protected Btn btnElim;
    
    // Table elements
    protected JTable table;
    protected JScrollPane scroll;

    public MainPanelBase(String title) {
        initBaseUI(title);
    }

    private void initBaseUI(String title) {
        setLayout(new BorderLayout());
        
        //USER MENU
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        navPanel = new JPanel(new BorderLayout());
        TopMenu nav = new TopMenu(); //crear menu
        navPanel.add(nav, BorderLayout.CENTER); //agregar a navpanel que a su hez esta en toppanel

        //TITULO
        titlePanel = new TitlePanel(title);

        // stretch horizontally
        navPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        topPanel.add(navPanel);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        //TABLE PANEL
        tablePanel = new JPanel(new BorderLayout());

        table = new JTable();
        scroll = new JScrollPane(table);

        tablePanel.add(scroll, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        
        //BUTTON PANEL

        btnPanel = new JPanel();
        add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setBorder(Styles.btnPanelPadding);
        
        btnAlta = Btn.primary("Alta");
        btnEdit = Btn.secondary("Editar");
        btnElim = Btn.secondary("Eliminar");
        
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        btnEdit.setPreferredSize(Styles.btnSizeSm);
        btnElim.setPreferredSize(Styles.btnSizeSm);
        
        btnPanel.setLayout(new FlowLayout());
        
        btnPanel.add(btnElim, FlowLayout.LEFT);
        btnPanel.add(btnEdit, FlowLayout.LEFT);
        btnPanel.add(btnAlta, FlowLayout.LEFT);
        
        // Connect search to table
        titlePanel.setTable(table);

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
    
    /* ========= Helper for cards ========= */
    protected <T> void setTableData(
        List<T> data,
        String[] columns,
        List<Function<T, Object>> getters) {

    @SuppressWarnings("unchecked")
    Function<T, Object>[] getterArray =
            getters.toArray(new Function[0]);

    CustomTableModel<T> model =
            new CustomTableModel<>(
                    data,
                    columns,
                    getterArray,
                    null, // setters optional
                    new boolean[columns.length]
            );

    table.setModel(model);
    titlePanel.setTable(table);
}



}

