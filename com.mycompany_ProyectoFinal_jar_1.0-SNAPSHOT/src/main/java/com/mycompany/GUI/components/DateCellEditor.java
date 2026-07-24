package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Editor de celda solo-fecha (dd/MM/yyyy) — sin hora. Usado por columnas "Fecha"
 * que ahora editan la fecha por separado de la columna "Hora" (ver TimeCellEditor).
 */
public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final Logger logger = LogManager.getLogger(DateCellEditor.class);
    private final JPanel container = new JPanel(new BorderLayout(2, 0));
    private final JTextField textField = new JTextField();
    private final JButton button = new JButton("📅");
    private JTable table;
    private JWindow popup;

    public DateCellEditor() {
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
                    e.consume(); // evita que JTable procese el mismo Enter y vuelva a intentar stopCellEditing()
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelCellEditing();
                    e.consume();
                }
            }
        });
    }

    private void openPicker(JTable anchor) {
        if (popup != null && popup.isVisible()) return;
        logger.debug("Opening Date picker");

        JCalendar calendar = new JCalendar(new java.util.Locale("es", "AR"));
        @SuppressWarnings("unchecked")
        JComboBox<String> monthCombo = (JComboBox<String>) calendar.getMonthChooser().getComboBox();
        monthCombo.setModel(new DefaultComboBoxModel<>(new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        }));
        LocalDate current = parse(textField.getText());
        if (current != null) {
            calendar.setDate(Date.from(current.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
                LocalDate ld = selected.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (ld.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
                    JOptionPane.showMessageDialog(popup,
                            "No se permite seleccionar fechas en domingo.",
                            "Fecha no permitida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                textField.setText(Styles.DATE.format(ld));
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

    private LocalDate parse(String text) {
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDate.parse(text.trim(), Styles.DATE);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean stopCellEditing() {
        closePopup();
        if (parse(textField.getText()) == null) {
            // El diálogo modal le roba el foco a la tabla: si terminateEditOnFocusLost sigue activo,
            // JTable reintenta stopCellEditing() mientras el diálogo sigue abierto y lo duplica.
            if (table != null) table.putClientProperty("terminateEditOnFocusLost", false);
            try {
                JOptionPane.showMessageDialog(container,
                        "Ingrese una fecha válida en formato dd/MM/yyyy\nEjemplo: 05/06/2026",
                        "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            } finally {
                if (table != null) table.putClientProperty("terminateEditOnFocusLost", true);
            }
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
