package com.mycompany.GUI.cards;

import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.AltaProductos;
import com.mycompany.proyectofinal.*;
import com.mycompany.proyectofinal.util.NumberVerifier;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;

public class ProductosSelectorDialog extends JDialog {

    private static final String PLACEHOLDER   = "Ej: 0.1";
    private static final Color  COLOR_PLACEHOLDER = Color.LIGHT_GRAY;
    private static final String PROP_PLACEHOLDER  = "isPlaceholder";

    private final Servicio servicio;
    private List<Producto> allProductos;
    private final Controladora control;
    private final Ventana ventana;

    private JPanel checkPanel;
    private final List<JCheckBox>  checkBoxes     = new ArrayList<>();
    private final List<JTextField> quantityFields = new ArrayList<>();
    private boolean saved = false;

    public ProductosSelectorDialog(Frame parent, Servicio servicio, List<Producto> allProductos,
                                   Controladora control, Ventana ventana) {
        super(parent, "Seleccionar Productos", true);
        this.servicio     = servicio;
        this.allProductos = new ArrayList<>(allProductos);
        this.control      = control;
        this.ventana      = ventana;
        initUI();
        setMinimumSize(new Dimension(480, 320));
        setLocationRelativeTo(parent);
    }

    // ─────────────────────────────────────────────────────────────
    // INIT
    // ─────────────────────────────────────────────────────────────

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JScrollPane scroll = new JScrollPane(checkPanel);
        scroll.setPreferredSize(new Dimension(460, 280));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo   = new JButton("Nuevo Producto");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnNuevo.addActionListener(e -> {
            Map<Integer, Double> current = captureCurrentState();
            AltaProductos d = new AltaProductos((Frame) getOwner(), true, () -> {});
            d.setLocationRelativeTo(this);
            d.setVisible(true);
            allProductos = control.traerProductos();
            rebuildContent(current);
        });

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnNuevo);
        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);

        rebuildContent(loadExistingSelections());
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT BUILDING
    // ─────────────────────────────────────────────────────────────

    private void rebuildContent(Map<Integer, Double> selectedWithQty) {
        checkPanel.removeAll();
        checkBoxes.clear();
        quantityFields.clear();

        checkPanel.add(buildHeaderRow());
        checkPanel.add(buildSeparator());

        for (Producto p : allProductos) {
            boolean isSelected = selectedWithQty.containsKey(p.getId());
            double  qty        = isSelected ? selectedWithQty.get(p.getId()) : 0.0;
            checkPanel.add(buildProductRow(p, isSelected, qty));
        }

        checkPanel.revalidate();
        checkPanel.repaint();
        pack();
    }

    private JPanel buildHeaderRow() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lSel    = boldLabel("",         25);
        JLabel lNombre = boldLabel("Producto", 160);
        JLabel lCant   = boldLabel("Cantidad",  85);
        JLabel lUnidad = boldLabel("Unidad",    70);

        header.add(lSel);
        header.add(Box.createHorizontalStrut(6));
        header.add(lNombre);
        header.add(Box.createHorizontalStrut(10));
        header.add(lCant);
        header.add(Box.createHorizontalStrut(6));
        header.add(lUnidad);
        return header;
    }

    private JPanel buildSeparator() {
        JPanel sep = new JPanel();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        return sep;
    }

    private JPanel buildProductRow(Producto p, boolean selected, double qty) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

        // Checkbox
        JCheckBox cb = new JCheckBox();
        cb.setSelected(selected);
        cb.setPreferredSize(new Dimension(25, 25));
        checkBoxes.add(cb);

        // Name
        JLabel nameLabel = new JLabel(p.getNombre());
        nameLabel.setPreferredSize(new Dimension(160, 25));
        nameLabel.setMaximumSize(new Dimension(160, 25));

        // Quantity input
        JTextField qtyField = buildQtyField(selected, qty);
        quantityFields.add(qtyField);

        // Unit label
        String unit = (p.getUnidad() != null && !p.getUnidad().isBlank()) ? p.getUnidad() : "—";
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setPreferredSize(new Dimension(70, 25));
        unitLabel.setMaximumSize(new Dimension(70, 25));
        unitLabel.setForeground(Color.GRAY);

        // Checkbox → enable/disable quantity field
        cb.addActionListener(e -> {
            boolean checked = cb.isSelected();
            qtyField.setEnabled(checked);
            if (!checked) {
                qtyField.setText("");
                qtyField.putClientProperty(PROP_PLACEHOLDER, false);
                qtyField.setForeground(UIManager.getColor("TextField.foreground"));
            } else {
                if (qtyField.getText().trim().isEmpty()) {
                    applyPlaceholder(qtyField);
                }
                qtyField.requestFocusInWindow();
            }
        });

        row.add(cb);
        row.add(Box.createHorizontalStrut(6));
        row.add(nameLabel);
        row.add(Box.createHorizontalStrut(10));
        row.add(qtyField);
        row.add(Box.createHorizontalStrut(6));
        row.add(unitLabel);
        return row;
    }

    private JTextField buildQtyField(boolean enabled, double qty) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(85, 25));
        field.setMaximumSize(new Dimension(85, 25));
        field.setMinimumSize(new Dimension(85, 25));
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setEnabled(enabled);

        if (qty > 0) {
            String text = qty % 1 == 0 ? String.valueOf((int) qty) : String.valueOf(qty);
            field.setText(text);
            field.setForeground(UIManager.getColor("TextField.foreground"));
            field.putClientProperty(PROP_PLACEHOLDER, false);
        } else if (enabled) {
            applyPlaceholder(field);
        } else {
            field.setText("");
            field.putClientProperty(PROP_PLACEHOLDER, false);
        }

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder(field)) clearPlaceholder(field);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) applyPlaceholder(field);
            }
        });

        field.addKeyListener(new NumberVerifier(true));
        return field;
    }

    // ─────────────────────────────────────────────────────────────
    // PLACEHOLDER HELPERS
    // ─────────────────────────────────────────────────────────────

    private void applyPlaceholder(JTextField field) {
        field.setText(PLACEHOLDER);
        field.setForeground(COLOR_PLACEHOLDER);
        field.putClientProperty(PROP_PLACEHOLDER, true);
    }

    private void clearPlaceholder(JTextField field) {
        field.setText("");
        field.setForeground(UIManager.getColor("TextField.foreground"));
        field.putClientProperty(PROP_PLACEHOLDER, false);
    }

    private boolean isPlaceholder(JTextField field) {
        return Boolean.TRUE.equals(field.getClientProperty(PROP_PLACEHOLDER));
    }

    /** Returns the real text, treating the placeholder as empty. */
    private String realText(JTextField field) {
        return isPlaceholder(field) ? "" : field.getText().trim();
    }

    // ─────────────────────────────────────────────────────────────
    // STATE HELPERS
    // ─────────────────────────────────────────────────────────────

    /** Read existing associations from the service entity. */
    private Map<Integer, Double> loadExistingSelections() {
        Map<Integer, Double> result = new HashMap<>();
        if (servicio.getProductos() != null) {
            for (ServicioProducto sp : servicio.getProductos()) {
                if (sp.getProducto() != null) {
                    result.put(sp.getProducto().getId(), sp.getCantidadUsada());
                }
            }
        }
        return result;
    }

    /** Snapshot current UI state so it can survive a product-list refresh. */
    private Map<Integer, Double> captureCurrentState() {
        Map<Integer, Double> result = new HashMap<>();
        for (int i = 0; i < allProductos.size() && i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                String text = realText(quantityFields.get(i));
                double qty  = 0.0;
                try { qty = Double.parseDouble(text); } catch (NumberFormatException ignored) {}
                result.put(allProductos.get(i).getId(), qty);
            }
        }
        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // VALIDATION
    // ─────────────────────────────────────────────────────────────

    private boolean validar() {
        for (int i = 0; i < allProductos.size() && i < checkBoxes.size(); i++) {
            if (!checkBoxes.get(i).isSelected()) continue;

            String nombre = allProductos.get(i).getNombre();
            String text   = realText(quantityFields.get(i));

            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Ingrese la cantidad para: " + nombre,
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
                quantityFields.get(i).requestFocusInWindow();
                return false;
            }

            double qty;
            try {
                qty = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Cantidad inválida para: " + nombre,
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a 0 para: " + nombre,
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
                quantityFields.get(i).requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────
    // SAVE
    // ─────────────────────────────────────────────────────────────

    private void guardar() {
        if (!validar()) return;

        // Audit: capture old state
        String oldValue = servicio.getProductos() == null ? "" :
            servicio.getProductos().stream()
                .filter(sp -> sp.getProducto() != null)
                .map(sp -> sp.getProducto().getNombre() + " ×" + sp.getCantidadUsada())
                .sorted()
                .collect(Collectors.joining(", "));

        // Remove all previous entries for this service
        for (ServicioProducto sp : new ArrayList<>(servicio.getProductos())) {
            servicio.removeProducto(sp);
        }

        // Insert checked products with their quantities
        for (int i = 0; i < allProductos.size() && i < checkBoxes.size(); i++) {
            if (!checkBoxes.get(i).isSelected()) continue;

            Producto p   = allProductos.get(i);
            double   qty = Double.parseDouble(realText(quantityFields.get(i)));

            ServicioProducto sp = new ServicioProducto();
            sp.setServicio(servicio);
            sp.setProducto(p);
            sp.setCantidadUsada(qty);
            servicio.addProducto(sp);
        }

        control.modificarServicio(servicio);

        // Audit: capture new state
        String newValue = servicio.getProductos().stream()
            .filter(sp -> sp.getProducto() != null)
            .map(sp -> sp.getProducto().getNombre() + " ×" + sp.getCantidadUsada())
            .sorted()
            .collect(Collectors.joining(", "));

        if (!oldValue.equals(newValue)) {
            RegistrarActividad.registrar(
                "SERVICIOS",
                String.valueOf(servicio.getId()),
                "productos",
                oldValue,
                newValue,
                "MODIFICACION"
            );
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    // ─────────────────────────────────────────────────────────────
    // UTILS
    // ─────────────────────────────────────────────────────────────

    private static JLabel boldLabel(String text, int width) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        lbl.setPreferredSize(new Dimension(width, 20));
        lbl.setMaximumSize(new Dimension(width, 20));
        return lbl;
    }
}
