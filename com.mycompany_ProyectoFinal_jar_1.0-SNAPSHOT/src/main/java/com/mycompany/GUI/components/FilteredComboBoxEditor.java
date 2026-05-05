package com.mycompany.GUI.components;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FilteredComboBoxEditor<T> extends AbstractCellEditor implements TableCellEditor {

    private static final String NEW_ITEM = "+ Nuevo...";

    private final JComboBox<Object> combo = new JComboBox<>();
    private final List<T> allItems;
    private final Function<T, String> label;
    private final Function<T, Integer> idOf;
    private final Supplier<List<T>> refresher;
    private final Runnable onNew;
    private Object lastValid;
    private boolean suppress = false;
    private JTable currentTable;

    public FilteredComboBoxEditor(List<T> items, Function<T, String> label,
                                  Function<T, Integer> idOf,
                                  Supplier<List<T>> refresher, Runnable onNew) {
        this.allItems = new ArrayList<>(items);
        this.label = label;
        this.idOf = idOf;
        this.refresher = refresher;
        this.onNew = onNew;

        combo.setEditable(true);
        combo.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        combo.addActionListener(e -> {
            if (suppress || combo.isPopupVisible()) return;
            Object selected = combo.getSelectedItem();
            if (selected == null || NEW_ITEM.equals(selected) || selected instanceof String) return;
            SwingUtilities.invokeLater(() -> {
                if (currentTable != null && currentTable.isEditing()) stopCellEditing();
            });
        });
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("");
                } else if (value instanceof String) {
                    setText((String) value);
                } else {
                    @SuppressWarnings("unchecked")
                    T item = (T) value;
                    setText(label.apply(item));
                }
                return this;
            }
        });

        JTextField editorField = (JTextField) combo.getEditor().getEditorComponent();
        editorField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(editorField); }
            public void removeUpdate(DocumentEvent e) { filter(editorField); }
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void populateModel(List<T> items) {
        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        for (T item : items) m.addElement(item);
        m.addElement(NEW_ITEM);
        combo.setModel(m);
    }

    private void filter(JTextField editorField) {
        if (suppress) return;
        String text = editorField.getText();
        SwingUtilities.invokeLater(() -> {
            if (suppress) return;
            suppress = true;
            try {
                String lower = text.toLowerCase();
                DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
                for (T item : allItems) {
                    if (label.apply(item).toLowerCase().contains(lower)) m.addElement(item);
                }
                m.addElement(NEW_ITEM);
                combo.setModel(m);
                editorField.setText(text);
                if (m.getSize() > 1) combo.showPopup();
            } finally {
                suppress = false;
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.currentTable = table;
        lastValid = value;
        suppress = true;
        try {
            populateModel(allItems);
            if (value != null && !(value instanceof String)) {
                @SuppressWarnings("unchecked")
                int targetId = idOf.apply((T) value);
                for (int i = 0; i < combo.getModel().getSize(); i++) {
                    Object it = combo.getModel().getElementAt(i);
                    if (!(it instanceof String)) {
                        @SuppressWarnings("unchecked")
                        int itId = idOf.apply((T) it);
                        if (itId == targetId) { combo.setSelectedItem(it); break; }
                    }
                }
            }
        } finally {
            suppress = false;
        }
        SwingUtilities.invokeLater(combo::showPopup);
        return combo;
    }

    @Override
    public Object getCellEditorValue() {
        Object selected = combo.getSelectedItem();
        if (selected == null || selected instanceof String) return lastValid;
        return selected;
    }

    @Override
    public boolean stopCellEditing() {
        if (NEW_ITEM.equals(combo.getSelectedItem())) {
            combo.setSelectedItem(lastValid);
            SwingUtilities.invokeLater(() -> {
                if (onNew != null) onNew.run();
                allItems.clear();
                allItems.addAll(refresher.get());
                suppress = true;
                try { populateModel(allItems); } finally { suppress = false; }
            });
            fireEditingStopped();
            return true;
        }
        return super.stopCellEditing();
    }

    public TableCellRenderer getRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value == null) { setText(""); return; }
                if (value instanceof String) { setText((String) value); return; }
                try {
                    @SuppressWarnings("unchecked")
                    T item = (T) value;
                    setText(label.apply(item));
                } catch (ClassCastException e) {
                    setText(value.toString());
                }
            }
        };
    }
}
