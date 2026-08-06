package com.mycompany.GUI.cards;

import com.mycompany.GUI.Ventana;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Cada fila de categoría siempre resuelve a un Producto concreto (se autoselecciona el de más
// stock al tildar la categoría, editable). Ese producto es el preferido al descontar stock; si
// se agota, el resto de la categoría actúa como respaldo automático — ver
// ControladoraPersistencia.descontarStockProductos.
public class ProductosSelectorDialog extends JDialog {

    private static final Logger logger = LogManager.getLogger(ProductosSelectorDialog.class);
    private static final String PLACEHOLDER   = "Ej: 0.1";
    private static final Color  COLOR_PLACEHOLDER = Color.LIGHT_GRAY;
    private static final String PROP_PLACEHOLDER  = "isPlaceholder";
    private static final String SIN_PRODUCTOS_EN_CATEGORIA = "(sin productos en esta categoría)";

    private final Servicio servicio;
    private List<Categoria> allCategorias;
    private final Controladora control;
    private final Ventana ventana;

    private JPanel checkPanel;
    private final List<JCheckBox>       checkBoxes     = new ArrayList<>();
    private final List<JTextField>      quantityFields = new ArrayList<>();
    private final List<JComboBox<Object>> productCombos = new ArrayList<>();
    private boolean saved = false;

    /** Selección existente para una categoría: cantidad + producto específico opcional (null = "cualquiera"). */
    private static class Seleccion {
        final double cantidad;
        final Producto producto;
        Seleccion(double cantidad, Producto producto) { this.cantidad = cantidad; this.producto = producto; }
    }

    public ProductosSelectorDialog(Frame parent, Servicio servicio,
                                   Controladora control, Ventana ventana) {
        super(parent, "Seleccionar Productos", true);
        this.servicio      = servicio;
        this.control       = control;
        this.ventana       = ventana;
        this.allCategorias = control.traerCategorias();
        initUI();
        setMinimumSize(new Dimension(600, 320));
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
        JButton btnNuevo    = new JButton("Nueva Categoría");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnNuevo.addActionListener(e -> crearNuevaCategoria());
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnNuevo);
        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);

        rebuildContent(loadExistingSelections());
    }

    // Mismo flujo que AltaProductos.crearNuevaCategoria(): reusa Controladora.crearCategoria.
    private void crearNuevaCategoria() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva categoría:");
        if (nombre == null || nombre.isBlank()) return;

        String[] unidades = {"ml", "gr", "unidades"};
        String unidad = (String) JOptionPane.showInputDialog(
            this, "Unidad de medida:", "Nueva Categoría",
            JOptionPane.QUESTION_MESSAGE, null, unidades, "ml");
        if (unidad == null) return;

        try {
            control.crearCategoria(nombre.trim(), unidad);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "La categoría \"" + nombre.trim() + "\" ya existe.",
                "Categoría duplicada", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (Exception e) {
            logger.error("Error al guardar la categoría '{}'", nombre.trim(), e);
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al guardar la categoría.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<Integer, Seleccion> current = captureCurrentState();
        allCategorias = control.traerCategorias();
        rebuildContent(current);
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT BUILDING
    // ─────────────────────────────────────────────────────────────

    private void rebuildContent(Map<Integer, Seleccion> seleccionPorCategoria) {
        checkPanel.removeAll();
        checkBoxes.clear();
        quantityFields.clear();
        productCombos.clear();

        checkPanel.add(buildHeaderRow());
        checkPanel.add(buildSeparator());

        for (Categoria c : allCategorias) {
            Seleccion sel = seleccionPorCategoria.get(c.getId());
            boolean isSelected = sel != null;
            double  qty        = isSelected ? sel.cantidad : 0.0;
            Producto prod      = isSelected ? sel.producto : null;
            checkPanel.add(buildCategoriaRow(c, isSelected, qty, prod));
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

        // Orden: Categoría | Producto | Cantidad | Unidad — igual que AltaServicios.
        JLabel lSel     = boldLabel("",          25);
        JLabel lNombre  = boldLabel("Categoría", 160);
        JLabel lProd    = boldLabel("Producto",  180);
        JLabel lCant    = boldLabel("Cantidad",   85);
        JLabel lUnidad  = boldLabel("Unidad",     70);

        header.add(lSel);
        header.add(Box.createHorizontalStrut(6));
        header.add(lNombre);
        header.add(Box.createHorizontalStrut(10));
        header.add(lProd);
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

    private JPanel buildCategoriaRow(Categoria c, boolean selected, double qty, Producto preseleccionado) {
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
        JLabel nameLabel = new JLabel(c.getNombre());
        nameLabel.setPreferredSize(new Dimension(160, 25));
        nameLabel.setMaximumSize(new Dimension(160, 25));

        // Producto — su elección determina qué se descuenta y de dónde sale la Unidad mostrada
        JComboBox<Object> prodCombo = buildProductoCombo(c, preseleccionado);
        prodCombo.setEnabled(selected);
        prodCombo.setPreferredSize(new Dimension(180, 25));
        prodCombo.setMaximumSize(new Dimension(180, 25));
        productCombos.add(prodCombo);

        // Quantity input
        JTextField qtyField = buildQtyField(selected, qty);
        quantityFields.add(qtyField);

        // Unidad — solo lectura, siempre derivada del modelo del Producto elegido (no de la
        // Categoria y no hardcodeada), se actualiza en vivo si se cambia el producto.
        JLabel unitLabel = new JLabel(unidadDe(prodCombo.getSelectedItem()));
        unitLabel.setPreferredSize(new Dimension(70, 25));
        unitLabel.setMaximumSize(new Dimension(70, 25));
        unitLabel.setForeground(Color.GRAY);
        prodCombo.addActionListener(e -> unitLabel.setText(unidadDe(prodCombo.getSelectedItem())));

        // Checkbox → enable/disable producto y cantidad
        cb.addActionListener(e -> {
            boolean checked = cb.isSelected();
            qtyField.setEnabled(checked);
            prodCombo.setEnabled(checked);
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
        row.add(prodCombo);
        row.add(Box.createHorizontalStrut(10));
        row.add(qtyField);
        row.add(Box.createHorizontalStrut(6));
        row.add(unitLabel);
        return row;
    }

    /** Unidad del producto elegido en el combo (o "—" si no hay uno real seleccionado). */
    private String unidadDe(Object prodSel) {
        if (prodSel instanceof Producto p && p.getUnidad() != null && !p.getUnidad().isBlank()) {
            return p.getUnidad();
        }
        return "—";
    }

    /** Combo de producto para una Categoria: productos de esa categoría, ordenados por stock
     *  descendente (el primero, o el ya seleccionado, queda como default). */
    private JComboBox<Object> buildProductoCombo(Categoria cat, Producto preseleccionado) {
        List<Producto> productosEnCat = control.traerProductos().stream()
            .filter(p -> p.getCategoria() != null && p.getCategoria().getId() == cat.getId())
            .sorted((a, b) -> Double.compare(b.getStock(), a.getStock()))
            .collect(Collectors.toList());

        JComboBox<Object> combo = new JComboBox<>();
        if (productosEnCat.isEmpty()) {
            combo.addItem(SIN_PRODUCTOS_EN_CATEGORIA);
        } else {
            int selectIndex = 0;
            for (Producto p : productosEnCat) {
                combo.addItem(p);
                if (preseleccionado != null && p.getId() == preseleccionado.getId()) {
                    selectIndex = combo.getItemCount() - 1;
                }
            }
            combo.setSelectedIndex(selectIndex);
        }

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value instanceof Producto p ? p.getNombre() : SIN_PRODUCTOS_EN_CATEGORIA);
                return this;
            }
        });
        return combo;
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
    private Map<Integer, Seleccion> loadExistingSelections() {
        Map<Integer, Seleccion> result = new HashMap<>();
        if (servicio.getProductos() != null) {
            for (ServicioProducto sp : servicio.getProductos()) {
                Categoria cat = sp.getCategoria();
                Producto prod = sp.getProducto();
                // Compatibilidad con ServicioProducto guardados por Producto puntual sin
                // Categoria — se ubican acá vía la categoría del producto.
                if (cat == null && prod != null) {
                    cat = prod.getCategoria();
                }
                if (cat != null) {
                    result.put(cat.getId(), new Seleccion(sp.getCantidadUsada(), prod));
                }
            }
        }
        return result;
    }

    /** Snapshot current UI state so it can survive a category-list refresh. */
    private Map<Integer, Seleccion> captureCurrentState() {
        Map<Integer, Seleccion> result = new HashMap<>();
        for (int i = 0; i < allCategorias.size() && i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                String text = realText(quantityFields.get(i));
                double qty  = 0.0;
                try { qty = Double.parseDouble(text); } catch (NumberFormatException ignored) {}
                Object prodSel = productCombos.get(i).getSelectedItem();
                Producto prod = prodSel instanceof Producto p ? p : null;
                result.put(allCategorias.get(i).getId(), new Seleccion(qty, prod));
            }
        }
        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // VALIDATION
    // ─────────────────────────────────────────────────────────────

    private boolean validar() {
        for (int i = 0; i < allCategorias.size() && i < checkBoxes.size(); i++) {
            if (!checkBoxes.get(i).isSelected()) continue;

            String nombre = allCategorias.get(i).getNombre();
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

            if (!(productCombos.get(i).getSelectedItem() instanceof Producto)) {
                JOptionPane.showMessageDialog(this,
                    "La categoría \"" + nombre + "\" no tiene productos cargados. "
                        + "Agregue un producto a esa categoría antes de continuar.",
                    "Sin productos", JOptionPane.WARNING_MESSAGE);
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
                .map(this::describirServicioProducto)
                .filter(s -> !s.isEmpty())
                .sorted()
                .collect(Collectors.joining(", "));

        // Remove all previous entries for this service
        for (ServicioProducto sp : new ArrayList<>(servicio.getProductos())) {
            servicio.removeProducto(sp);
        }

        // Insert checked categories with their quantities — validar() ya garantizó que cada una
        // tiene un producto concreto asignado. Ese producto es el preferido al descontar stock;
        // si se agota, el resto de la categoría actúa como respaldo (ver descontarStockProductos).
        for (int i = 0; i < allCategorias.size() && i < checkBoxes.size(); i++) {
            if (!checkBoxes.get(i).isSelected()) continue;

            double qty = Double.parseDouble(realText(quantityFields.get(i)));
            Object prodSel = productCombos.get(i).getSelectedItem();
            if (!(prodSel instanceof Producto prod)) continue;

            ServicioProducto sp = new ServicioProducto();
            sp.setServicio(servicio);
            sp.setCantidadUsada(qty);
            sp.setProducto(prod);
            servicio.addProducto(sp);
        }

        control.modificarServicio(servicio);

        // Audit: capture new state
        String newValue = servicio.getProductos().stream()
            .map(this::describirServicioProducto)
            .filter(s -> !s.isEmpty())
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

    private String describirServicioProducto(ServicioProducto sp) {
        if (sp.getProducto()  != null) return sp.getProducto().getNombre()  + " ×" + sp.getCantidadUsada();
        if (sp.getCategoria() != null) return sp.getCategoria().getNombre() + " ×" + sp.getCantidadUsada();
        return "";
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
