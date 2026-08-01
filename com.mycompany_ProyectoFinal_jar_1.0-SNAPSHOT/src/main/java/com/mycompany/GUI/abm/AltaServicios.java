/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.GUI.abm;

import com.mycompany.GUI.Styles;
import com.mycompany.proyectofinal.Categoria;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author duart
 */
public class AltaServicios extends JDialog {

    private static final Logger logger = LogManager.getLogger(AltaServicios.class);
    Controladora control = new Controladora();
    private Runnable onSave;
    Usuario empleadoSelec;
    private JTable tablaCategorias;
    private DefaultTableModel modeloCategorias;
    private Servicio servEditar;
    
  //MODO ALTA
    public AltaServicios(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        
        initComponents();
        
        obtenerUsuarios();
        inicializarTablaCategorias();
        cargarCategorias();

        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        btnPanel.add(btnAlta);

        initUI();

        btnAlta.addActionListener(e -> guardarServicio());
    }

    //MODO MODIFICAR
    public AltaServicios(Frame parent, boolean modal, Servicio serv, Runnable onSave) {
        super(parent, modal);
        initComponents();
        this.servEditar = serv;
        this.onSave = onSave;

        obtenerUsuarios();
        inicializarTablaCategorias();
        cargarCategorias();

        cargarDatosServicio(); // cargar datos en los campos
        
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        btnPanel.add(btnAlta);
        btnAlta.addActionListener(e -> guardarServicio());
        
        initUI();
    }
   
    
    private void initUI(){
    
    
        Btn btnLimpiar = Btn.secondary("Limpiar");
        btnLimpiar.setPreferredSize(Styles.btnSizeSm);
        btnPanel.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        btnPanel.add(btnCerrar);

        jPanel2.setBackground(Styles.bgLight);
        jPanel3.setBackground(Styles.bgLight);
        btnPanel.setBackground(Styles.bgLight);

        btnLimpiar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtNombre.setText("");
                    txtPrecio.setText("");
                    txtDuracion.setText("");
                    cboEmpleados.setSelectedIndex(-1);
                    //cboProductos.setSelectedIndex(-1);
                }
        });

        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });

        // Precio admite decimales; Duración es un entero de minutos (ver parseInt en guardarServicio).
        txtPrecio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) && c != '.' && c != ',' && c != '\b') {
                    e.consume();
                }
            }
        });

        txtDuracion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) return;
                if (!Character.isDigit(c)) e.consume();
            }
        });

    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblCargaEmp = new javax.swing.JLabel();
        btnPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cboEmpleados = new javax.swing.JComboBox<>();
        scrollProd = new javax.swing.JScrollPane();
        jLabel8 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaEmp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaEmp.setText("Carga de Servicios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(lblCargaEmp)
                .addContainerGap(249, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(lblCargaEmp)
                .addGap(29, 29, 29))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        btnPanel.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.add(btnPanel, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Nombre*");

        jLabel2.setText("Precio*");

        txtDuracion.setBackground(new java.awt.Color(242, 242, 242));
        txtDuracion.setForeground(new java.awt.Color(102, 102, 102));
        txtDuracion.setText("60");
        txtDuracion.setBorder(null);
        txtDuracion.setPreferredSize(new java.awt.Dimension(73, 30));

        txtNombre.setBackground(new java.awt.Color(242, 242, 242));
        txtNombre.setForeground(new java.awt.Color(102, 102, 102));
        txtNombre.setText("Servicio");
        txtNombre.setBorder(null);
        txtNombre.setPreferredSize(new java.awt.Dimension(73, 30));
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        jLabel6.setText("Empleado");

        jLabel7.setText("Productos");

        jLabel8.setText("Duración");

        txtPrecio.setBackground(new java.awt.Color(242, 242, 242));
        txtPrecio.setForeground(new java.awt.Color(102, 102, 102));
        txtPrecio.setText("10000");
        txtPrecio.setBorder(null);
        txtPrecio.setPreferredSize(new java.awt.Dimension(73, 30));

        jLabel9.setText("Min.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollProd, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDuracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboEmpleados, javax.swing.GroupLayout.Alignment.LEADING, 0, 122, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cboEmpleados, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDuracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                        .addGap(177, 177, 177))
                    .addComponent(scrollProd))
                .addGap(91, 91, 91))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

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

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void inicializarTablaCategorias() {
        modeloCategorias = new DefaultTableModel(
            new Object[]{"Sel.", "Categoría", "Cant."}, 0
        ) {
            @Override public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                if (col == 2) return Double.class;
                return Object.class;
            }
            @Override public boolean isCellEditable(int row, int col) { return col == 0 || col == 2; }
        };
        tablaCategorias = new JTable(modeloCategorias);
        tablaCategorias.setRowHeight(25);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblCats = new JLabel("Productos por categoría:");
        lblCats.setFont(lblCats.getFont().deriveFont(Font.BOLD));
        panel.add(lblCats);
        panel.add(Box.createVerticalStrut(4));
        panel.add(tablaCategorias.getTableHeader());
        panel.add(tablaCategorias);

        scrollProd.setViewportView(panel);
    }

    private boolean validarCampos() {
        if (
            txtNombre.getText().isEmpty() ||
            txtPrecio.getText().isEmpty() ||
            txtDuracion.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        
        
        return true; // Indicate validation success
    }
    
    //cargar servicios a cbo
    public void obtenerUsuarios(){
        List<Usuario> usuarios = control.traerUsuarios();
        List<String> nombres = new ArrayList<>();
        for (Usuario usu : usuarios) {
            String apellido = usu.getApellido();
            String nombre = usu.getNombre();
            String nombreComp;
            nombreComp = nombre + " " + apellido;
            cboEmpleados.addItem(nombreComp); 
            nombres.add(nombreComp);
        }
        Styles.addAutoComplete(cboEmpleados, nombres);
    }
    //encontrar servicio por nombre y guardar
    public Usuario guardarEmpleado(String emp){
        List<Usuario> usuarios = control.traerUsuarios();
        Usuario usuarioSeleccionado = null;

        for (Usuario usu : usuarios) {
            String nombreComp = usu.getNombre() + " " + usu.getApellido();
            
                if (nombreComp.equals(emp)) {
                    usuarioSeleccionado = usu;
                    break;
                }
        }
        

        
        if (usuarioSeleccionado != null) {
            return usuarioSeleccionado;
        } else {
            return null;
        }
    }
    
    
    
    private void cargarCategorias() {
        modeloCategorias.setRowCount(0);
        for (Categoria c : control.traerCategorias()) {
            modeloCategorias.addRow(new Object[]{false, c, 0.0});
        }
    }

    // Productos/categorías son opcionales: un Servicio puede crearse sin ninguna seleccionada.
    // Solo se valida que, SI se seleccionó una categoría, tenga una cantidad > 0 cargada.
    private boolean validarCategorias() {
        if (tablaCategorias.isEditing()) tablaCategorias.getCellEditor().stopCellEditing();
        for (int i = 0; i < modeloCategorias.getRowCount(); i++) {
            Boolean sel = (Boolean) modeloCategorias.getValueAt(i, 0);
            if (sel == null || !sel) continue;
            Object cantObj = modeloCategorias.getValueAt(i, 2);
            double cantidad = cantObj instanceof Number ? ((Number) cantObj).doubleValue() : 0;
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad de cada categoría seleccionada debe ser mayor a 0.",
                    "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }


    
    private void guardarServicio() {

    if (!validarCampos() || !validarCategorias()) {
        return;
    }

    Servicio servicio;

    if (servEditar == null) {
        servicio = new Servicio();
    } else {
        servicio = servEditar;
        servicio.getProductos().clear(); // importante en edición
    }
    
    //validar precio
    double precio;

        try {
            precio = Double.parseDouble(txtPrecio.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un número válido.");
            return; // ← aquí estaba el problema
        }
        servicio.setNombre(txtNombre.getText());
        servicio.setPrecio(precio);

        int duracion;
        try {
            duracion = Integer.parseInt(txtDuracion.getText().trim());
            if (duracion <= 0) duracion = 60;
        } catch (NumberFormatException e) {
            duracion = 60;
        }
        servicio.setDuracionMinutos(duracion);

        if (cboEmpleados.getSelectedItem() != null) {
            String empleado = (String) cboEmpleados.getSelectedItem();
            empleadoSelec = guardarEmpleado(empleado);
            servicio.setEmpleado(empleadoSelec);
        }

    // Categorías seleccionadas — el descuento greedy elige el producto con más stock dentro de cada categoría
    for (int i = 0; i < modeloCategorias.getRowCount(); i++) {
        Boolean sel = (Boolean) modeloCategorias.getValueAt(i, 0);
        if (sel == null || !sel) continue;
        Object ref = modeloCategorias.getValueAt(i, 1);
        Object cantObj = modeloCategorias.getValueAt(i, 2);
        double cantidad = cantObj instanceof Number ? ((Number) cantObj).doubleValue() : 0;
        if (cantidad <= 0) continue;
        if (ref instanceof Categoria cat) {
            ServicioProducto sp = new ServicioProducto();
            sp.setCantidadUsada(cantidad);
            sp.setCategoria(cat);
            servicio.addProducto(sp);
        }
    }

    try {
        if (servEditar == null) {
            control.guardarServicio(servicio);
            RegistrarActividad.registrar(
                "SERVICIOS",
                "nuevo registro",
                "alta",
                null,
                "Nombre: " + servicio.getNombre() + " | Precio: " + precio + " | Empleado: " + (empleadoSelec != null ? empleadoSelec.getNombre() + " " + empleadoSelec.getApellido() : "N/A"),
                "ALTA"
            );
            JOptionPane.showMessageDialog(this,
                    "Servicio guardado correctamente.",
                    "Alta exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            control.modificarServicio(servicio);
            JOptionPane.showMessageDialog(this,
                    "Servicio modificado correctamente.",
                    "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if (onSave != null) {
            onSave.run();
        }

        dispose();
    } catch (Exception e) {
        logger.error("Error al guardar el servicio", e);
        JOptionPane.showMessageDialog(this,
            "Ocurrió un error al guardar el servicio.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void cargarDatosServicio() {
        txtNombre.setText(servEditar.getNombre());
        txtPrecio.setText(String.valueOf(servEditar.getPrecio()));
        int dur = servEditar.getDuracionMinutos();
        txtDuracion.setText(String.valueOf(dur > 0 ? dur : 60));

        if (servEditar.getEmpleado() != null) {
            String nombreEmpleado = servEditar.getEmpleado().getNombre() + " " + servEditar.getEmpleado().getApellido();
            for (int i = 0; i < cboEmpleados.getItemCount(); i++) {
                if (nombreEmpleado.equals(cboEmpleados.getItemAt(i))) {
                    cboEmpleados.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Pre-selecciona las categorías ya asociadas al servicio
        for (ServicioProducto sp : servEditar.getProductos()) {
            if (sp.getCategoria() == null) continue;
            for (int i = 0; i < modeloCategorias.getRowCount(); i++) {
                Object ref = modeloCategorias.getValueAt(i, 1);
                if (ref instanceof Categoria ct && ct.getId() == sp.getCategoria().getId()) {
                    modeloCategorias.setValueAt(true, i, 0);
                    modeloCategorias.setValueAt(sp.getCantidadUsada(), i, 2);
                    break;
                }
            }
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnPanel;
    private javax.swing.JComboBox<String> cboEmpleados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JScrollPane scrollProd;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
