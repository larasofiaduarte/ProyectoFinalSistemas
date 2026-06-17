/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;
import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.*;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.GUI.components.FilteredComboBoxEditor;
import com.mycompany.proyectofinal.util.CategoriaOptions;
import com.mycompany.proyectofinal.util.PrediccionStock;
import com.mycompany.proyectofinal.util.StockAlerta;
import com.mycompany.proyectofinal.util.StockFormatter;
import com.mycompany.proyectofinal.Proveedor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Inventario extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;
    private JPanel alertasPanel;

    public Inventario(Ventana ventana) {
        super("Inventario");
        this.ventana = ventana;
        this.control = new Controladora();
        inicializarPanelAlertas(); // debe ir antes de initUI() → cargarTabla()
        initUI();
    }

    private void initUI() {
        cargarTabla();

        btnAlta.addActionListener(e -> abrirAltaProducto());
        btnElim.addActionListener(e -> eliminarProducto());
        // btnEdit.addActionListener(e -> modificarProducto()); // disabled — editing is handled inline
        titlePanel.addReportButtonListener(e -> generarReport());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (!table.isEditing() && row >= 0 && table.isCellEditable(row, col)) {
                    if (col == colIndex("Proveedor")) {
                        table.editCellAt(row, col, e);
                        Component comp = table.getEditorComponent();
                        if (comp != null) comp.requestFocusInWindow();
                    }
                }
            }
        });
    }
    
    public void cargarTabla(){
        java.util.List<Producto> productos = control.traerProductos();

        String[] columns = {
            "ID", "Nombre", "Stock", "Unidad", "Minimo", "Categoria", "Proveedor"
        };

        java.util.List<Function<Producto, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> StockFormatter.format(c.getStock(), c.getUnidad()),
            c -> {
                String u = c.getUnidad();
                // Si el stock supera 1 litro, la unidad efectiva de display es "lt"
                if ("ml".equals(u) && c.getStock() >= 1000) return "lt";
                return u != null ? u : "ml";
            },
            c -> StockFormatter.format(c.getMinimo(), c.getUnidad()),
            c -> c.getCategoria() != null ? c.getCategoria() : "",
            c -> c.getProveedor()
        );

        // Unidad (col 3) es solo lectura — se actualiza automáticamente cuando se edita stock o mínimo
        boolean[] editables = new boolean[columns.length];
        for (int i = 1; i < columns.length; i++) editables[i] = true;
        editables[0] = false;
        editables[3] = false;
        setTableData(productos, columns, getters, editables);

        @SuppressWarnings("unchecked")
        CustomTableModel<Producto> prodModel = (CustomTableModel<Producto>) table.getModel();
        prodModel.setValueSetter(1, (p, v) -> p.setNombre(v.toString()));
        prodModel.setValueSetter(2, (p, v) -> {
            try {
                // Parsea "1000ml", "1lt", "250gr" o número puro (mantiene unidad actual)
                StockFormatter.ParseResult r = StockFormatter.parseConUnidad(v.toString(), p.getUnidad());
                p.setStock(r.valor);
                p.setUnidad(r.unidad);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                    "Formato inválido. Usá: 1000ml, 1lt, 0.5lt, 250gr",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            }
        });
        // col 3 (Unidad) no tiene setter — es de solo lectura
        prodModel.setValueSetter(4, (p, v) -> {
            try {
                // Igual que stock: parsea con unidad y actualiza el campo de unidad
                StockFormatter.ParseResult r = StockFormatter.parseConUnidad(v.toString(), p.getUnidad());
                p.setMinimo(r.valor);
                p.setUnidad(r.unidad);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                    "Formato inválido. Usá: 1000ml, 1lt, 0.5lt, 250gr",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            }
        });
        prodModel.setValueSetter(5, (p, v) -> {
            if (v != null && !"+ Nuevo...".equals(v.toString()))
                p.setCategoria(v.toString());
        });
        prodModel.setValueSetter(6, (p, v) -> p.setProveedor((Proveedor) v));
        prodModel.setEntityClass(Producto.class, Map.of(1, "nombre", 5, "categoria"));
        prodModel.setTableName("PRODUCTOS");
        prodModel.setOnPersist(p -> {
            control.modificarProducto(p, p.getNombre(), p.getStock(), p.getMinimo(), p.getProveedor(), p.getCategoria());
            showToast("Cambio guardado");
        });

        // Recalcula las alertas de predicción cada vez que se recarga la tabla
        actualizarAlertas();

        List<Proveedor> proveedores = control.traerProveedores();

        SwingUtilities.invokeLater(() -> {
            // Editor de stock — acepta sufijos de unidad: "1000ml", "1lt", "250gr"
            JTextField stockField = new JTextField();
            stockField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (Character.isISOControl(c)) return;
                    if (Character.isDigit(c) || c == '.' || c == 'm' || c == 'l' || c == 't' || c == 'g' || c == 'r') return;
                    e.consume();
                }
            });
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(stockField));

            // Editor de mínimo — misma lógica de unidades que stock
            JTextField minimoField = new JTextField();
            minimoField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    char c = e.getKeyChar();
                    if (Character.isISOControl(c)) return;
                    if (Character.isDigit(c) || c == '.' || c == 'm' || c == 'l' || c == 't' || c == 'g' || c == 'r') return;
                    e.consume();
                }
            });
            table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(minimoField));

            // Editor de categoría: dropdown con opciones predefinidas + opción de nueva categoría
            List<String> catItems = new ArrayList<>(Arrays.asList(CategoriaOptions.OPCIONES));
            catItems.add("+ Nuevo...");
            JComboBox<String> comboCat = new JComboBox<>(catItems.toArray(new String[0]));
            comboCat.addActionListener(e -> {
                if ("+ Nuevo...".equals(comboCat.getSelectedItem())) {
                    comboCat.hidePopup();
                    String nueva = JOptionPane.showInputDialog(table, "Nombre de la nueva categoría:");
                    if (nueva != null && !nueva.isBlank()) {
                        String cat = nueva.trim();
                        // Inserta la nueva opción antes de "+ Nuevo..." y la selecciona
                        comboCat.insertItemAt(cat, comboCat.getItemCount() - 1);
                        comboCat.setSelectedItem(cat);
                    } else {
                        comboCat.setSelectedIndex(0);
                    }
                }
            });
            table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(comboCat));

            // Editor de proveedor con búsqueda filtrada
            int colProv = colIndex("Proveedor");
            FilteredComboBoxEditor<Proveedor> provEditor = new FilteredComboBoxEditor<>(
                proveedores,
                Proveedor::getNombre,
                Proveedor::getId,
                control::traerProveedores,
                () -> {
                    AltaProveedores d = new AltaProveedores(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colProv).setCellEditor(provEditor);
            table.getColumnModel().getColumn(colProv).setCellRenderer(provEditor.getRenderer());
        });
    }
    
    /** Crea el panel de alertas y lo ancla encima de la tabla. */
    private void inicializarPanelAlertas() {
        alertasPanel = new JPanel();
        alertasPanel.setLayout(new BoxLayout(alertasPanel, BoxLayout.Y_AXIS));
        alertasPanel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        alertasPanel.setVisible(false);
        tablePanel.add(alertasPanel, BorderLayout.NORTH);
    }

    /** Recalcula y muestra las alertas de stock basadas en turnos pendientes del próximo 14 días. */
    private void actualizarAlertas() {
        if (alertasPanel == null) return;
        List<StockAlerta> alertas = PrediccionStock.generarAlertas(
            control.traerTurnos(), control.traerProductos()
        );
        alertasPanel.removeAll();
        if (!alertas.isEmpty()) {
            JLabel titulo = new JLabel("  Predicción de stock — próximos 14 días:");
            titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 12f));
            titulo.setForeground(Color.DARK_GRAY);
            alertasPanel.add(titulo);
            alertasPanel.add(Box.createVerticalStrut(4));
            for (StockAlerta a : alertas) {
                JLabel lbl = new JLabel("  " + formatearAlerta(a));
                lbl.setForeground(
                    a.getNivel() == StockAlerta.Nivel.CRITICAL
                        ? new Color(185, 28, 28)   // rojo
                        : new Color(146, 64, 14)   // naranja oscuro
                );
                alertasPanel.add(lbl);
            }
        }
        alertasPanel.setVisible(!alertas.isEmpty());
        alertasPanel.revalidate();
        alertasPanel.repaint();
    }

    /** Formatea el texto de una alerta individual para mostrar en el panel. */
    private String formatearAlerta(StockAlerta a) {
        String disponible = StockFormatter.format(a.getStockActual(), a.getUnidad());
        String necesario  = StockFormatter.format(a.getConsumoProyectado(), a.getUnidad());
        if (a.getNivel() == StockAlerta.Nivel.CRITICAL) {
            return "⚠ " + a.getProductoNombre()
                + " — se agota: " + disponible + " disponible, " + necesario + " necesarios";
        } else {
            return "⚡ " + a.getProductoNombre()
                + " — bajo stock: " + disponible + " disponible, " + necesario + " necesarios";
        }
    }

    private void abrirAltaProducto() {
        AltaProductos dialog =
        new AltaProductos(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarProducto() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.",
                    "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        DeleteWithRelationsHandler.handleDeleteProducto(this, id, () ->
            JOptionPane.showMessageDialog(this, "Producto borrado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE)
        );

        cargarTabla();
    }
    
    // private void modificarProducto() { // disabled — editing is handled inline
    //     int fila = table.getSelectedRow();
    //     if (fila == -1) {
    //         JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
    //         return;
    //     }
    //     int id = ((Number) table.getValueAt(fila, 0)).intValue();
    //     Producto prod = control.findProducto(id);
    //     AltaProductos dialog = new AltaProductos(ventana, true, prod, this::cargarTabla);
    //     dialog.setLocationRelativeTo(this);
    //     dialog.setVisible(true);
    // }
    
    public void seleccionarProducto(Producto p) {
        @SuppressWarnings("unchecked")
        com.mycompany.GUI.components.CustomTableModel<Producto> model =
            (com.mycompany.GUI.components.CustomTableModel<Producto>) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getRowObject(i).getId() == p.getId()) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }
    
    private void generarReport() {
        String[] opciones = {"PDF", "DOCX"};
        String formato = (String) JOptionPane.showInputDialog(
            this,
            "Seleccionar formato de exportación:",
            "Exportar Reporte",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            "PDF"
        );
        if (formato != null) {
            ReportManager.generateReport(this, "inventario.jrxml", null, "ListaStock", formato);
        }
    }
}
