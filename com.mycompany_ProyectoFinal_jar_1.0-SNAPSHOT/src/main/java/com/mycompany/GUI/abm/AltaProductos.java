
package com.mycompany.GUI.abm;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.util.CategoriaOptions;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Usuario;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JOptionPane;


public class AltaProductos extends JDialog{
    
    private static final String NUEVA_OPCION = "+ Nuevo proveedor...";

    Controladora control = new Controladora();
    private Runnable onSave;
    Proveedor provSelec;
    private Producto prodEditar;
    private final java.util.List<String> nombresProveedores = new ArrayList<>();

    //MODO ALTA
    public AltaProductos(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        
        initComponents();
        
        obtenerProveedores(); // carga cbo
        
        
        //UI
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
        cargarDatosProducto(); // cargar datos en los campos
        
        //UI
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);
        
        initUI();
        
        btnAlta.addActionListener(e -> guardarProducto());
    }
    
    //BOTONES
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
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != ',') {
                    e.consume();
                }
            }
        });

        txtMinimo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != ',') {
                    e.consume();
                }
            }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtNombre.setText("");
                    txtStock.setText("");
                    txtMinimo.setText("");
                    cmbCategoria.setSelectedIndex(0);
                    cmbUnidad.setSelectedIndex(0);
                }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
    }
    
    private void guardarProducto() {

        String nombre  = txtNombre.getText();
        Double stock   = Double.parseDouble(txtStock.getText().replace(",", "."));
        Double minimo  = Double.parseDouble(txtMinimo.getText().replace(",", "."));
        String unidad  = (String) cmbUnidad.getSelectedItem();
        // Si el usuario ingresó litros, convierte a ML antes de guardar (1 LT = 1000 ML)
        if ("lt".equalsIgnoreCase(unidad)) {
            stock = stock * 1000;
            unidad = "ml";
        }
        // Toma el valor seleccionado o escrito en el combo (soporta opciones predefinidas y texto libre)
        Object catObj = cmbCategoria.getSelectedItem();
        String categoria = catObj != null ? catObj.toString().trim() : "";

        String prov = (String) cboProv.getSelectedItem();
        provSelec = guardarProveedor(prov);

        if (validarCampos()){
            if (provSelec == null) {
                JOptionPane.showMessageDialog(null, "Proveedor no encontrado. Por favor, seleccione un proveedor válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the action if prov does not exist
            }else{
                control.guardarProducto(nombre, stock, minimo, provSelec, unidad, categoria.isEmpty() ? null : categoria);
                if (prodEditar == null) {
                    RegistrarActividad.registrar(
                        "PRODUCTOS",
                        "nuevo registro",
                        "alta",
                        null,
                        "Nombre: " + nombre + " | Stock: " + stock + " | Mínimo: " + minimo + " | Proveedor: " + (provSelec != null ? provSelec.getNombre() : "N/A"),
                        "ALTA"
                    );
                }
                JOptionPane.showMessageDialog(null, "Producto guardado correctamente.", "Producto guardado.", JOptionPane.INFORMATION_MESSAGE);
                if (onSave != null) {
                    onSave.run();   // 👈 refresh table
                }
                dispose();
            }
                        
        }

    }
    
    private void cargarDatosProducto() {
        txtNombre.setText(prodEditar.getNombre());
        txtStock.setText(Double.toString(prodEditar.getStock()));
        txtMinimo.setText(Double.toString(prodEditar.getMinimo()));
        String unidadVal = prodEditar.getUnidad();
        cmbUnidad.setSelectedItem(unidadVal != null ? unidadVal : "ml");
        if (cmbUnidad.getSelectedIndex() < 0) cmbUnidad.setSelectedIndex(0);
        String cat = prodEditar.getCategoria();
        if (cat != null && !cat.isEmpty()) {
            cmbCategoria.setSelectedItem(cat);
            // Si el valor guardado no está en la lista predefinida, lo muestra igual en el editor
            cmbCategoria.getEditor().setItem(cat);
        }
        cboProv.setSelectedItem(prodEditar.getProveedor());
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
        cmbUnidad.addItem("gr");
        cmbUnidad.addItem("unidades");
        cmbUnidad.setSelectedIndex(0);
        jLabel5 = new javax.swing.JLabel();
        jLabelCategoria = new javax.swing.JLabel();
        // Opciones predefinidas de categoría desde la lista centralizada; editable para valores personalizados
        cmbCategoria = new javax.swing.JComboBox<>(CategoriaOptions.OPCIONES);
        cmbCategoria.setEditable(true);
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
                    .addComponent(jLabel5)
                    .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCategoria)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        if (
                txtMinimo.getText().isEmpty() ||
                txtStock.getText().isEmpty()  ||
                txtNombre.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        return true; // Indicate validation success
    }
    
    //cargar servicios a cbo
    public void obtenerProveedores() {
        nombresProveedores.clear();
        java.util.List<Proveedor> proveedores = control.traerProveedores();
        for (Proveedor p : proveedores) nombresProveedores.add(p.getNombre());
        nombresProveedores.add(NUEVA_OPCION);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombresProveedores) model.addElement(n);
        cboProv.setModel(model);

        cboProv.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (NUEVA_OPCION.equals(value)) {
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                }
                return this;
            }
        });

        Styles.addAutoComplete(cboProv, nombresProveedores);

        // addAutoComplete replaces the model on each key release — re-append sentinel afterward
        JTextField editor = (JTextField) cboProv.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE ||
                    key == KeyEvent.VK_UP    || key == KeyEvent.VK_DOWN) return;
                DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) cboProv.getModel();
                if (m.getIndexOf(NUEVA_OPCION) < 0) m.addElement(NUEVA_OPCION);
            }
        });

        cboProv.addActionListener(e -> {
            if (NUEVA_OPCION.equals(cboProv.getSelectedItem())) {
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
        java.util.List<Proveedor> actualizados = control.traerProveedores();

        nombresProveedores.clear();
        for (Proveedor p : actualizados) nombresProveedores.add(p.getNombre());
        nombresProveedores.add(NUEVA_OPCION);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombresProveedores) model.addElement(n);
        cboProv.setModel(model);

        if (!actualizados.isEmpty()) {
            cboProv.setSelectedItem(actualizados.get(actualizados.size() - 1).getNombre());
        }
    }
    //encontrar servicio por nombre y guardar el seleccionado
    public Proveedor guardarProveedor(String proveedor){
        java.util.List<Proveedor> proveedores = control.traerProveedores();
        Proveedor proveedorSeleccionado = null;

        for (Proveedor prov : proveedores) {
            String nombre = prov.getNombre();

                if (nombre.equals(proveedor)) {
                    proveedorSeleccionado = prov;
                    break;
                }
        }

        
        if (proveedorSeleccionado != null) {
            return proveedorSeleccionado;
        } else {
            return null;
        }
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
    private javax.swing.JComboBox<String> cmbCategoria;
    // End of variables declaration//GEN-END:variables
}
