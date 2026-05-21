
package com.mycompany.GUI.abm;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.util.EmailVerifier;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import com.mycompany.proyectofinal.util.NumberVerifier;
import com.mycompany.proyectofinal.Proveedor;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
        


public class AltaProveedores extends JDialog {
    
    Controladora control = new Controladora();
    private Runnable onSave;
    private Proveedor provEditar;
    
    //MODO ALTA 
    public AltaProveedores(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        
        initComponents();
        //panelNorth.setBorder(Styles.paddingTop);
        //panelSouth.setBorder(Styles.paddingBottom);
        panelCenter.setBackground(Styles.bgLight);
        
        JLabel lblTitle = new JLabel("Carga de Proveedores");
        lblTitle.setFont(Styles.fontTitle);
        lblTitle.setForeground(Styles.fontDark);
        panelNorth.add(lblTitle);
        
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelSouth.add(btnAlta);
        
        initUI();
        
        btnAlta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nombre = txtNombre.getText();
                    String telefono = txtTelefono.getText();
                    String email = txtCorreo.getText();
                    String web = txtWeb.getText();
                    
                    if(txtNombre.getText() != null && !txtNombre.getText().isEmpty()){
                        control.guardarProveedor(nombre, telefono, email, web);
                        RegistrarActividad.registrar(
                            "PROVEEDORES",
                            "nuevo registro",
                            "alta",
                            null,
                            "Nombre: " + nombre + " | Teléfono: " + telefono + " | Email: " + email,
                            "ALTA"
                        );
                        JOptionPane.showMessageDialog(null, "Proveedor guardado correctamente.", "Proveedor guardado.", JOptionPane.INFORMATION_MESSAGE);
                        if (onSave != null) {
                            onSave.run();   // 👈 refresh table
                        }
                        dispose();
                    
                    }else{
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                        
                    }
                    
                    
                }
        });
        
    }
    
    // MODO MODIFICAR
    public AltaProveedores(Frame parent, boolean modal, Proveedor prov, Runnable onSave) {
        super(parent, modal);
        initComponents();
        this.provEditar = prov;
        this.onSave = onSave;

        cargarDatosProveedor(); // cargar datos en los campos
        
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelSouth.add(btnAlta);
        btnAlta.addActionListener(e -> guardarProveedor());
        
        initUI();
    }

    //BOTONES UI 
    private void initUI(){
        
        
        txtCorreo.setInputVerifier(new EmailVerifier());

        Btn btnLimpiar = Btn.secondary("Limpiar");
        btnLimpiar.setPreferredSize(Styles.btnSizeSm);
        panelSouth.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        panelSouth.add(btnCerrar);
        
        panelNorth.setBackground(Styles.bgLight);
        jPanel1.setBackground(Styles.bgLight);
        panelSouth.setBackground(Styles.bgLight);
        
        
        txtTelefono.addKeyListener(new NumberVerifier());
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtNombre.setText("");
                    txtTelefono.setText("");
                    txtCorreo.setText("");
                    txtWeb.setText("");
                }
        });
        
       
    }
    
    //btn Alta event
     private void guardarProveedor() {
         if (txtNombre.getText().trim().isEmpty()){
             JOptionPane.showMessageDialog(this, "Debe asignar un nombre al Proveedor.");
             return;
         }
         if (!txtCorreo.getInputVerifier().verify(txtCorreo)) {
            return;
        }

        String nombre = txtNombre.getText();
                    String telefono = txtTelefono.getText();
                    String email = txtCorreo.getText();
                    String web = txtWeb.getText();

        if (provEditar == null) {
            // MODO ALTA
            control.guardarProveedor(nombre, telefono, email, web);
            RegistrarActividad.registrar(
                "PROVEEDORES",
                "nuevo registro",
                "alta",
                null,
                "Nombre: " + nombre + " | Teléfono: " + telefono + " | Email: " + email,
                "ALTA"
            );
            JOptionPane.showMessageDialog(this, "Proveedor guardado correctamente.");
        } else {
            //MODO MODIFICAR
            control.modificarProveedor(provEditar, nombre, telefono, email, web);
            JOptionPane.showMessageDialog(this, "Proveedor modificado correctamente.");
        }

        onSave.run();
        dispose();
    }
     
     
     
     private void cargarDatosProveedor() {
        txtNombre.setText(provEditar.getNombre());
        txtTelefono.setText(provEditar.getTelefono());
        txtCorreo.setText(provEditar.getEmail());
        txtWeb.setText(provEditar.getWebsite());
        
    }
     
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelNorth = new javax.swing.JPanel();
        panelSouth = new javax.swing.JPanel();
        panelCenter = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtWeb = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(250, 250, 250));

        jPanel1.setLayout(new java.awt.BorderLayout());

        panelNorth.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.add(panelNorth, java.awt.BorderLayout.PAGE_START);

        panelSouth.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.add(panelSouth, java.awt.BorderLayout.PAGE_END);

        panelCenter.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Nombre*");

        jLabel2.setText("Teléfono");

        jLabel3.setText("Correo Electrónico");

        txtNombre.setBackground(new java.awt.Color(240, 240, 240));
        txtNombre.setForeground(new java.awt.Color(102, 102, 102));
        txtNombre.setText("Proveedor");
        txtNombre.setBorder(null);
        txtNombre.setPreferredSize(new java.awt.Dimension(220, 30));
        txtNombre.setSelectionColor(new java.awt.Color(204, 204, 255));

        txtTelefono.setBackground(new java.awt.Color(240, 240, 240));
        txtTelefono.setForeground(new java.awt.Color(102, 102, 102));
        txtTelefono.setText("1234 567890");
        txtTelefono.setBorder(null);
        txtTelefono.setPreferredSize(new java.awt.Dimension(220, 30));
        txtTelefono.setSelectionColor(new java.awt.Color(204, 204, 255));

        txtCorreo.setBackground(new java.awt.Color(240, 240, 240));
        txtCorreo.setForeground(new java.awt.Color(102, 102, 102));
        txtCorreo.setText("Correo@gmail.com");
        txtCorreo.setBorder(null);
        txtCorreo.setPreferredSize(new java.awt.Dimension(220, 30));
        txtCorreo.setSelectionColor(new java.awt.Color(204, 204, 255));

        jLabel4.setText("Sitio Web");

        txtWeb.setBackground(new java.awt.Color(240, 240, 240));
        txtWeb.setForeground(new java.awt.Color(102, 102, 102));
        txtWeb.setText("www.Web.com");
        txtWeb.setBorder(null);
        txtWeb.setPreferredSize(new java.awt.Dimension(220, 30));
        txtWeb.setSelectionColor(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout panelCenterLayout = new javax.swing.GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenterLayout.setHorizontalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)))
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCenterLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCenterLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(txtWeb, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        panelCenterLayout.setVerticalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(25, 25, 25)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtWeb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(239, Short.MAX_VALUE))
        );

        jPanel1.add(panelCenter, java.awt.BorderLayout.CENTER);

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelCenter;
    private javax.swing.JPanel panelNorth;
    private javax.swing.JPanel panelSouth;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtWeb;
    // End of variables declaration//GEN-END:variables
}
