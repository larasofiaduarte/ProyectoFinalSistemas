package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class DateTimeCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanel container = new JPanel(new BorderLayout(2, 0));
    private final JTextField textField = new JTextField();
    private final JButton button = new JButton("📅");
    private JTable table;
    private JWindow popup;

    public DateTimeCellEditor() {
        container.add(textField, BorderLayout.CENTER);

        button.addActionListener(e -> openPicker(table));

        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (popup == null || !popup.isVisible()) {
                    openPicker(table);
                }
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelCellEditing();
                }
            }
        });
    }

    private void openPicker(JTable anchor) {
        if (popup != null && popup.isVisible()) return;
        System.out.println("Opening DateTime picker");

        JCalendar calendar = new JCalendar(new java.util.Locale("es", "AR"));
        @SuppressWarnings("unchecked")
        JComboBox<String> monthCombo = (JComboBox<String>) calendar.getMonthChooser().getComboBox();
        monthCombo.setModel(new DefaultComboBoxModel<>(new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        }));
        LocalDateTime current = parse(textField.getText());
        if (current != null) {
            calendar.setDate(Date.from(current.atZone(ZoneId.systemDefault()).toInstant()));
        }

        popup = new JWindow(SwingUtilities.getWindowAncestor(anchor != null ? anchor : container));
        JPanel calPanel = new JPanel(new BorderLayout());
        calPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        calPanel.add(calendar, BorderLayout.CENTER);
        popup.add(calPanel);
        popup.pack();

        try {
            Point loc = container.getLocationOnScreen();
            popup.setLocation(loc.x, loc.y + container.getHeight());
        } catch (IllegalComponentStateException ex) {
            popup.setLocationRelativeTo(anchor);
        }

        // Prevent terminateEditOnFocusLost from killing the editor while picker is open
        if (table != null) table.putClientProperty("terminateEditOnFocusLost", false);

        popup.setVisible(true);

        // Auto-close and update field when a day is clicked
        calendar.getDayChooser().addPropertyChangeListener("day", e -> {
            Date selected = calendar.getDate();
            if (selected != null) {
                LocalDateTime ldt = selected.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
                if (ldt.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
                    JOptionPane.showMessageDialog(popup,
                            "No se permite seleccionar fechas en domingo.",
                            "Fecha no permitida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                textField.setText(Styles.DATE_TIME.format(ldt));
            }
            closePopup();
        });

        // Close when clicking anywhere outside the popup
        AWTEventListener[] listenerHolder = new AWTEventListener[1];
        listenerHolder[0] = event -> {
            if (event instanceof MouseEvent me && me.getID() == MouseEvent.MOUSE_PRESSED) {
                if (popup != null && !popup.getBounds().contains(me.getLocationOnScreen())) {
                    closePopup();
                    Toolkit.getDefaultToolkit().removeAWTEventListener(listenerHolder[0]);
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listenerHolder[0], AWTEvent.MOUSE_EVENT_MASK);

        // Restore terminateEditOnFocusLost and clean up listener when popup is disposed
        popup.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Toolkit.getDefaultToolkit().removeAWTEventListener(listenerHolder[0]);
                if (table != null) table.putClientProperty("terminateEditOnFocusLost", true);
            }
        });
    }

    private void closePopup() {
        if (popup != null) {
            popup.dispose();
            popup = null;
        }
    }

    private LocalDateTime parse(String text) {
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDateTime.parse(text.trim(), Styles.DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean stopCellEditing() {
        closePopup();
        if (parse(textField.getText()) == null) {
            JOptionPane.showMessageDialog(container,
                    "Ingrese una fecha válida en formato dd/MM/yyyy HH:mm\nEjemplo: 05/06/2026 14:30",
                    "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        closePopup();
        super.cancelCellEditing();
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText().trim();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        textField.setText(value != null ? value.toString() : "");
        SwingUtilities.invokeLater(() -> openPicker(table));
        return container;
    }
}
