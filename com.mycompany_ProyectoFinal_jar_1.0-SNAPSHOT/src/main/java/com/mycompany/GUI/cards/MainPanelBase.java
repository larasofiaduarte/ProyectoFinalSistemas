
package com.mycompany.GUI.cards;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyListener;
import com.mycompany.GUI.Styles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.mycompany.GUI.components.ReportBtn;
import com.mycompany.GUI.components.*;
import com.mycompany.proyectofinal.util.TelefonoVerifier;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.util.function.Function;

public abstract class MainPanelBase extends JPanel {

    protected TitlePanel titlePanel;
    protected JPanel topPanel;
    protected JPanel tablePanel;
    protected JPanel btnPanel;
    protected JPanel navPanel;
    protected JPanel actionsRowPanel;
    protected JPanel filterDropPanel;
    protected Btn btnFilter;
    protected List<Btn> filterOptionsList = new ArrayList<>();
    
    protected Btn btnAlta;
    protected Btn btnEdit;
    protected Btn btnElim;
    protected ReportBtn btnReport;
    
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

        actionsRowPanel = new JPanel(new BorderLayout());
        actionsRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        actionsRowPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(19, 0, 0, 0));

        btnFilter = Btn.filter("Filtrar");
        btnFilter.setPreferredSize(Styles.btnSizeSm);
        FlatSVGIcon filterIcon = new FlatSVGIcon("images/filters.svg", 16, 16);
        btnFilter.setIcon(filterIcon);
        btnFilter.setHorizontalTextPosition(SwingConstants.LEFT);
        btnFilter.setIconTextGap(8);
        leftPanel.add(btnFilter);

        actionsRowPanel.add(leftPanel, BorderLayout.WEST);
        actionsRowPanel.add(titlePanel.getActionsPanel(), BorderLayout.EAST);
        topPanel.add(actionsRowPanel);

        // Drop-down filter strip — lives in topPanel below the actions row
        filterDropPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        filterDropPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 6, 20));
        filterDropPanel.setVisible(false);

        Btn btnIdAsc    = Btn.filterOption("ID Ascendente");
        Btn btnIdDesc   = Btn.filterOption("ID Descendente");
        Btn btnReciente = Btn.filterOption("Más reciente");
        Btn btnAntiguo  = Btn.filterOption("Más antiguo");

        filterOptionsList.addAll(List.of(btnIdAsc, btnIdDesc, btnReciente, btnAntiguo));

        for (Btn b : filterOptionsList) {
            b.setPreferredSize(new Dimension(150, 28));
            filterDropPanel.add(b);
        }

        btnIdAsc.addActionListener(e -> {
            filterOptionsList.forEach(b -> b.setSelectedState(false));
            btnIdAsc.setSelectedState(true);
            applySortKey(0, SortOrder.ASCENDING);
        });
        btnIdDesc.addActionListener(e -> {
            filterOptionsList.forEach(b -> b.setSelectedState(false));
            btnIdDesc.setSelectedState(true);
            applySortKey(0, SortOrder.DESCENDING);
        });
        btnReciente.addActionListener(e -> {
            filterOptionsList.forEach(b -> b.setSelectedState(false));
            btnReciente.setSelectedState(true);
            int col = findDateColumn(); if (col >= 0) applySortKey(col, SortOrder.DESCENDING);
        });
        btnAntiguo.addActionListener(e -> {
            filterOptionsList.forEach(b -> b.setSelectedState(false));
            btnAntiguo.setSelectedState(true);
            int col = findDateColumn(); if (col >= 0) applySortKey(col, SortOrder.ASCENDING);
        });

        btnIdAsc.setSelectedState(true);

        topPanel.add(filterDropPanel);

        btnFilter.addActionListener(e -> {
            filterDropPanel.setVisible(!filterDropPanel.isVisible());
            topPanel.revalidate();
            topPanel.repaint();
        });

        add(topPanel, BorderLayout.NORTH);

        //TABLE PANEL
        tablePanel = new JPanel(new BorderLayout());

        table = new JTable();
        table.putClientProperty("terminateEditOnFocusLost", true);
        scroll = new JScrollPane(table);

        tablePanel.add(scroll, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        
        //BUTTON PANEL

        btnPanel = new JPanel();
        add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setBorder(Styles.btnPanelPadding);
        
        btnAlta = Btn.primary("Alta");
        // btnEdit = Btn.secondary("Editar"); // disabled — editing is handled inline
        btnElim = Btn.secondary("Eliminar");

        btnAlta.setPreferredSize(Styles.btnSizeSm);
        // btnEdit.setPreferredSize(Styles.btnSizeSm);
        btnElim.setPreferredSize(Styles.btnSizeSm);

        btnPanel.setLayout(new FlowLayout());

        btnPanel.add(btnElim, FlowLayout.LEFT);
        // btnPanel.add(btnEdit, FlowLayout.LEFT);
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

        if (actionsRowPanel != null) {
            actionsRowPanel.setBackground(Styles.bgLight);
        }

        if (filterDropPanel != null) {
            filterDropPanel.setBackground(Styles.bgLight);
        }

        repaint();
    }
    
    
    protected <T> void setTableData(
    List<T> data,
    String[] columns,
    List<Function<T, Object>> getters) {

    setTableData(data, columns, getters, defaultEditables(columns.length));
}
    
    protected <T> void setTableData(
        List<T> data,
        String[] columns,
        List<Function<T, Object>> getters,
        boolean[] editables) {

        Function<T, Object>[] getterArray = getters.toArray(new Function[0]);
        CustomTableModel<T> model = new CustomTableModel<>(
            data,
            columns,
            getterArray,
            null,
            editables  // ← usá el array que pasás
        );
        table.setModel(model);
        titlePanel.setTable(table);
        attachPhoneVerifier(columns);
        TableUtils.applyDefaultIdSorting(table);
    }

    private void attachPhoneVerifier(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            if (!"Teléfono".equals(columns[i])) continue;

            TableColumn col = table.getColumnModel().getColumn(i);
            TableCellEditor editor = col.getCellEditor();

            if (editor instanceof DefaultCellEditor dce) {
                Component comp = dce.getComponent();
                if (comp instanceof JTextField field && !hasPhoneVerifier(field)) {
                    field.addKeyListener(new TelefonoVerifier(TelefonoVerifier.Mode.PHONE));
                }
            } else if (editor == null) {
                // No explicit editor: JTable default for Object/String is JTextField-based.
                // Create a dedicated editor so the verifier has a concrete field to attach to.
                JTextField field = new JTextField();
                field.addKeyListener(new TelefonoVerifier(TelefonoVerifier.Mode.PHONE));
                col.setCellEditor(new DefaultCellEditor(field));
            }
            // else: FilteredComboBoxEditor or other custom editor → skip
            break;
        }
    }

    private boolean hasPhoneVerifier(JTextField field) {
        for (KeyListener kl : field.getKeyListeners()) {
            if (kl instanceof TelefonoVerifier) return true;
        }
        return false;
    }

    // permitir editar celdas
    protected boolean[] defaultEditables(int columnCount) {
        boolean[] e = new boolean[columnCount];
        for (int i = 1; i < columnCount; i++) {
            e[i] = true;
        }
        return e;
    }

    protected int colIndex(String name) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (name.equals(table.getColumnName(i))) return i;
        }
        return -1;
    }

    protected void applySortKey(int column, SortOrder order) {
        RowSorter<?> rs = table.getRowSorter();
        if (rs instanceof TableRowSorter<?> sorter) {
            sorter.setSortKeys(List.of(new RowSorter.SortKey(column, order)));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setColumnComparator(int column, Comparator<Object> comparator) {
        RowSorter<?> rs = table.getRowSorter();
        if (rs instanceof TableRowSorter sorter) {
            sorter.setComparator(column, comparator);
        }
    }

    protected void addFilterOption(String label, Runnable action) {
        Btn b = Btn.filterOption(label);
        b.setPreferredSize(new Dimension(150, 28));
        b.addActionListener(e -> {
            filterOptionsList.forEach(opt -> opt.setSelectedState(false));
            b.setSelectedState(true);
            action.run();
        });
        filterOptionsList.add(b);
        filterDropPanel.add(b);
        filterDropPanel.revalidate();
    }

    private int findDateColumn() {
        for (int i = 0; i < table.getColumnCount(); i++) {
            String name = table.getColumnName(i).toLowerCase();
            if (name.contains("fecha") || name.contains("date")) return i;
        }
        return -1;
    }

    protected void showToast(String message) {
        JWindow toast = new JWindow(SwingUtilities.getWindowAncestor(this));
        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        label.setOpaque(true);
        label.setBackground(new Color(60, 60, 60));
        label.setForeground(Color.WHITE);
        toast.getContentPane().add(label);
        toast.pack();
        if (isShowing()) {
            Point loc = getLocationOnScreen();
            Dimension panelSize = getSize();
            Dimension toastSize = toast.getSize();
            toast.setLocation(loc.x + panelSize.width - toastSize.width - 20,
                              loc.y + panelSize.height - toastSize.height - 20);
        }
        toast.setVisible(true);
        new Timer(2000, e -> toast.dispose()).start();
    }

}

