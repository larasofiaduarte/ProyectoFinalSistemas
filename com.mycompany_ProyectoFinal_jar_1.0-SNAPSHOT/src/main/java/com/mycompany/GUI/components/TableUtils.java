package com.mycompany.GUI.components;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import java.util.List;

public final class TableUtils {

    private TableUtils() {}

    /**
     * Applies a fixed ID-descending sort to any JTable backed by CustomTableModel.
     * Column header clicks are disabled so sorting stays under programmatic control.
     */
    public static void applyDefaultIdSorting(JTable table) {
        if (!(table.getModel() instanceof CustomTableModel)) {
            return;
        }

        // Reuse the sorter already set by TitlePanel so search filtering stays wired up.
        // Only create a new one if none exists yet.
        TableRowSorter<TableModel> sorter;
        if (table.getRowSorter() instanceof TableRowSorter) {
            @SuppressWarnings("unchecked")
            TableRowSorter<TableModel> existing = (TableRowSorter<TableModel>) table.getRowSorter();
            sorter = existing;
        } else {
            sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
        }

        // Column 0 is always the ID; Number cast handles both Integer and Long
        sorter.setComparator(0, Comparator.comparingInt(a -> ((Number) a).intValue()));

        // Disable header-click sorting on every column
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            sorter.setSortable(i, false);
        }

        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }
}
