
package com.mycompany.GUI.cards;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyListener;
import com.mycompany.GUI.Styles;
import java.util.List;
import com.mycompany.GUI.components.ReportBtn;
import com.mycompany.GUI.components.*;
import com.mycompany.proyectofinal.util.TelefonoVerifier;
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

