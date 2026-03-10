/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import com.mycompany.GUI.Styles;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import com.mycompany.GUI.*;
public class TitlePanel extends JPanel implements Theme {

    private SearchBar search;
    private TableRowSorter<?> sorter;

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel filterPanel;
    private JLabel lblTitle;
    private JPanel reportPanel;
    private ReportBtn btnReport;

    public TitlePanel(String title) {
        setLayout(new BorderLayout());
        setBorder(Styles.padding);

        topPanel = new JPanel(new BorderLayout());
        bottomPanel = new JPanel(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Title
        lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(Styles.fontTitle);
        topPanel.add(lblTitle, BorderLayout.WEST);

        // Filters panel
        filterPanel = new JPanel();
        bottomPanel.add(filterPanel, BorderLayout.WEST);
        bottomPanel.setBorder(Styles.searchPadding);
        
        //report btn
        btnReport = new ReportBtn();

        reportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(reportPanel);
        reportPanel.add(btnReport);
        
        // Search field
        search = new SearchBar("Buscar");
        search.setPreferredSize(new Dimension(260, 40));
        bottomPanel.add(search, BorderLayout.EAST);

        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals("Buscar")) {
                    search.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText("Buscar");
                }
            }
        });

        applyTheme(); // apply initial theme
    }

    public void setTable(JTable table) {
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
    }

    private void filterTable() {
        if (sorter == null) return;

        String text = search.getText();

        if (text.trim().isEmpty() || text.equals("Buscar")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    @Override
    public void applyTheme() {
        setBackground(Styles.bgLight);

        topPanel.setBackground(Styles.bgLight);
        bottomPanel.setBackground(Styles.bgLight);
        filterPanel.setBackground(Styles.bgLight);

        lblTitle.setForeground(Styles.fontDark);

        search.setBackground(Styles.bgSearch);
        search.setForeground(Styles.bgDark);
        search.setCaretColor(Styles.bgDark);

        repaint();
    }
    
    
}
