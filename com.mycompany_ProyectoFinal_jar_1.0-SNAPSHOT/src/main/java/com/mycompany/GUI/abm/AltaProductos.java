
package com.mycompany.GUI.abm;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Categoria;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Proveedor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AltaProductos extends JDialog{

    private static final Logger logger = LogManager.getLogger(AltaProductos.class);
    private static final String NUEVA_PROV_OPCION    = "+ Nuevo proveedor...";
    private static final String NUEVA_CAT_OPCION     = "+ Nueva categoría...";

    Controladora control = new Controladora();
    private Runnable onSave;
    Proveedor provSelec;
    private Producto prodEditar;
    private final List<String> nombresProveedores = new ArrayList<>();

    //MODO ALTA
    public AltaProductos(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;

        initComponents();
        obtenerProveedores();
        obtenerCategorias();

        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);

        initUI();

        btnAlta.addActionListener(e -> guardarProducto());
    }

    // MODO MODIFICAR
    public AltaProductos(Frame parent, boolean modal, Producto prod, Runnable onSave) {
        super(parent, modal);
        initComponents();
        this.prodEditar = prod;
        this.onSave = onSave;
        obtenerProveedores();
        obtenerCategorias();
        cargarDatosProducto();

        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);

        initUI();

        btnAlta.addActionListener(e -> guardarProducto());
    }

    private void initUI(){
        Btn btnLimpiar = Btn.secondary("Limpiar");
        btnLimpiar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnLimpiar);

        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnCerrar);

        jPanel2.setBackground(Styles.bgLight);
        jPanel1.setBackground(Styles.bgLight);
        panelBtns.setBackground(Styles.bgLight);

        txtStock.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != ',') e.consume();
            }
        });
        txtMinimo.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != ',') e.consume();
            }
        });

        // Actualiza cmbUnidad según la categoría seleccionada
        cmbCategoria.addActionListener(e -> {
            Object sel = cmbCategoria.getSelectedItem();
            if (NUEVA_CAT_OPCION.equals(sel)) {
                cmbCategoria.hidePopup();
                crearNuevaCategoria();
            } else if (sel instanceof Categoria cat) {
                actualizarCmbUnidad(cat.getUnidad());
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtNombre.setText("");
            txtStock.setText("");
            txtMinimo.setText("");
            if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
            cmbUnidad.setSelectedIndex(0);
        });

        btnCerrar.addActionListener(e -> dispose());
    }

    private void actualizarCmbUnidad(String unidad) {
        cmbUnidad.removeAllItems();
        if ("ml".equals(unidad)) {
            cmbUnidad.addItem("ml");
            cmbUnidad.addItem("lt");
            cmbUnidad.setEnabled(true);
        } else {
            cmbUnidad.addItem(unidad != null ? unidad : "ml");
            cmbUnidad.setEnabled(false);
        }
        cmbUnidad.setSelectedIndex(0);
    }

    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        if (!validarCampos()) return;

        double stock  = Double.parseDouble(txtStock.getText().replace(",", "."));
        double minimo = Double.parseDouble(txtMinimo.getText().replace(",", "."));

        Object catObj = cmbCategoria.getSelectedItem();
        if (!(catObj instanceof Categoria)) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una categoría válida.",
                "Categoría requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Categoria categoriaSelec = (Categoria) catObj;

        // Convierte litros → ml si la categoría usa ml como unidad base
        String inputUnidad = (String) cmbUnidad.getSelectedItem();
        if ("lt".equalsIgnoreCase(inputUnidad) && "ml".equals(categoriaSelec.getUnidad())) {
            stock = stock * 1000;
        }

        String provNombre = (String) cboProv.getSelectedItem();
        provSelec = guardarProveedor(provNombre);
        if (provSelec == null) {
            JOptionPane.showMessageDialog(this,
                "Proveedor no encontrado. Por favor, seleccione un proveedor válido.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            control.guardarProducto(nombre, stock, minimo, provSelec, categoriaSelec);
            if (prodEditar == null) {
                RegistrarActividad.registrar(
                    "PRODUCTOS", "nuevo registro", "alta", null,
                    "Nombre: " + nombre + " | Stock: " + stock + " | Mínimo: " + minimo
                        + " | Categoría: " + categoriaSelec.getNombre()
                        + " | Proveedor: " + provSelec.getNombre(),
                    "ALTA"
                );
            }
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.",
                "Producto guardado.", JOptionPane.INFORMATION_MESSAGE);
            if (onSave != null) onSave.run();
            dispose();
        } catch (Exception e) {
            logger.error("Error al guardar el producto", e);
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al guardar el producto.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosProducto() {
        txtNombre.setText(prodEditar.getNombre());
        txtStock.setText(Double.toString(prodEditar.getStock()));
        txtMinimo.setText(Double.toString(prodEditar.getMinimo()));

        Categoria cat = prodEditar.getCategoria();
        if (cat != null) {
            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                Object item = cmbCategoria.getItemAt(i);
                if (item instanceof Categoria c && c.getId() == cat.getId()) {
                    cmbCategoria.setSelectedIndex(i);
                    break;
                }
            }
            actualizarCmbUnidad(cat.getUnidad());
        }
        cboProv.setSelectedItem(prodEditar.getProveedor() != null
            ? prodEditar.getProveedor().getNombre() : null);
    }

    /** Abre un diálogo para crear una nueva categoría y recarga el combo. */
    private void crearNuevaCategoria() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva categoría:");
        if (nombre == null || nombre.isBlank()) {
            reseleccionarPrimeraCat();
            return;
        }
        String[] unidades = {"ml", "gr", "unidades"};
        String unidad = (String) JOptionPane.showInputDialog(
            this, "Unidad de medida:", "Nueva Categoría",
            JOptionPane.QUESTION_MESSAGE, null, unidades, "ml");
        if (unidad == null) {
            reseleccionarPrimeraCat();
            return;
        }
        try {
            control.crearCategoria(nombre.trim(), unidad);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "La categoría \"" + nombre.trim() + "\" ya existe.",
                "Categoría duplicada", JOptionPane.WARNING_MESSAGE);
            reseleccionarPrimeraCat();
            return;
        } catch (Exception e) {
            logger.error("Error al guardar la categoría '{}'", nombre.trim(), e);
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al guardar la categoría.",
                "Error", JOptionPane.ERROR_MESSAGE);
            reseleccionarPrimeraCat();
            return;
        }
        obtenerCategorias();
        // Auto-seleccionar la recién creada
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Object item = cmbCategoria.getItemAt(i);
            if (item instanceof Categoria c && c.getNombre().equals(nombre.trim())) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void reseleccionarPrimeraCat() {
        if (cmbCategoria.getItemCount() > 0
                && cmbCategoria.getItemAt(0) instanceof Categoria) {
            cmbCategoria.setSelectedIndex(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblCargaEmp = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtMinimo = new javax.swing.JTextField();
        cboProv = new javax.swing.JComboBox<>();
        cmbUnidad = new javax.swing.JComboBox<>();
        cmbUnidad.addItem("ml");
        cmbUnidad.addItem("lt");
        cmbUnidad.setSelectedIndex(0);
        jLabel5 = new javax.swing.JLabel();
        jLabelCategoria = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        cmbCategoria.setPreferredSize(new java.awt.Dimension(145, 30));
        panelBtns = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaEmp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaEmp.setText("Carga de Productos");

        jPanel2.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Nombre*");

        jLabel2.setText("Stock Actual*");

        jLabel3.setText("Stock Minimo*");

        jLabel4.setText("Proveedor");

        txtStock.setBackground(new java.awt.Color(242, 242, 242));
        txtStock.setForeground(new java.awt.Color(102, 102, 102));
        txtStock.setText("1");
        txtStock.setBorder(null);
        txtStock.setPreferredSize(new java.awt.Dimension(73, 30));

        txtNombre.setBackground(new java.awt.Color(242, 242, 242));
        txtNombre.setForeground(new java.awt.Color(102, 102, 102));
        txtNombre.setText("Nombre Producto");
        txtNombre.setBorder(null);
        txtNombre.setPreferredSize(new java.awt.Dimension(73, 30));

        txtMinimo.setBackground(new java.awt.Color(242, 242, 242));
        txtMinimo.setForeground(new java.awt.Color(102, 102, 102));
        txtMinimo.setText("1");
        txtMinimo.setBorder(null);
        txtMinimo.setPreferredSize(new java.awt.Dimension(73, 30));

        cboProv.setToolTipText("");

        cmbUnidad.setPreferredSize(new java.awt.Dimension(145, 30));

        jLabel5.setText("Unidad*");

        jLabelCategoria.setText("Categoría");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabelCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(txtStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboProv, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cboProv, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCategoria)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelBtns.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelBtns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(lblCargaEmp)
                .addContainerGap(164, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblCargaEmp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBtns, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private boolean validarCampos() {
        if (txtMinimo.getText().isEmpty() ||
            txtStock.getText().isEmpty()  ||
            txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos obligatorios.",
                "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** Carga categorías desde la BD y arma el combo con la opción "+ Nueva categoría...". */
    public void obtenerCategorias() {
        List<Categoria> cats = control.traerCategorias();
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        for (Categoria c : cats) model.addElement(c);
        model.addElement(NUEVA_CAT_OPCION);
        cmbCategoria.setModel(model);

        cmbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (NUEVA_CAT_OPCION.equals(value)) {
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                }
                return this;
            }
        });

        // Selecciona la primera categoría y sincroniza cmbUnidad
        if (!cats.isEmpty()) {
            cmbCategoria.setSelectedIndex(0);
            actualizarCmbUnidad(cats.get(0).getUnidad());
        }
    }

    public void obtenerProveedores() {
        nombresProveedores.clear();
        List<Proveedor> proveedores = control.traerProveedores();
        for (Proveedor p : proveedores) nombresProveedores.add(p.getNombre());
        nombresProveedores.add(NUEVA_PROV_OPCION);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombresProveedores) model.addElement(n);
        cboProv.setModel(model);

        cboProv.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (NUEVA_PROV_OPCION.equals(value)) {
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                }
                return this;
            }
        });

        Styles.addAutoComplete(cboProv, nombresProveedores);

        JTextField editor = (JTextField) cboProv.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE ||
                    key == KeyEvent.VK_UP    || key == KeyEvent.VK_DOWN) return;
                DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) cboProv.getModel();
                if (m.getIndexOf(NUEVA_PROV_OPCION) < 0) m.addElement(NUEVA_PROV_OPCION);
            }
        });

        cboProv.addActionListener(e -> {
            if (NUEVA_PROV_OPCION.equals(cboProv.getSelectedItem())) {
                cboProv.hidePopup();
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(cboProv);
                AltaProveedores dialog = new AltaProveedores(parent, true, () -> {});
                dialog.setLocationRelativeTo(AltaProductos.this);
                dialog.setVisible(true);
                recargarProveedores();
            }
        });
    }

    private void recargarProveedores() {
        List<Proveedor> actualizados = control.traerProveedores();
        nombresProveedores.clear();
        for (Proveedor p : actualizados) nombresProveedores.add(p.getNombre());
        nombresProveedores.add(NUEVA_PROV_OPCION);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombresProveedores) model.addElement(n);
        cboProv.setModel(model);
        if (!actualizados.isEmpty()) {
            cboProv.setSelectedItem(actualizados.get(actualizados.size() - 1).getNombre());
        }
    }

    public Proveedor guardarProveedor(String proveedor) {
        for (Proveedor prov : control.traerProveedores()) {
            if (prov.getNombre().equals(proveedor)) return prov;
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboProv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JPanel panelBtns;
    private javax.swing.JComboBox<String> cmbUnidad;
    private javax.swing.JTextField txtMinimo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtStock;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JComboBox<Object> cmbCategoria;
    // End of variables declaration//GEN-END:variables
}
