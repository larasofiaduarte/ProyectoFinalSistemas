
package com.mycompany.GUI.abm;

import com.mycompany.proyectofinal.Controladora;
import java.awt.Font;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.NumberVerifier;
import com.mycompany.proyectofinal.Usuario;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.Date;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class AltaEmpleados extends JDialog {

    Controladora control = new Controladora();
    private Runnable onSave;
    private Usuario userEditar;
    
    //MODO ALTA
    public AltaEmpleados(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        
        initComponents();
        jLabel8.setForeground(Styles.fontDark);
        jLabel3.setForeground(Styles.fontDark);
        
        //UI
        panelAltaEmp.setBackground(Styles.bgLight);
        panelDataEmp.setBackground(Styles.bgLight);
        
        cboEmpRol.addItem("Empleado");
        cboEmpRol.addItem("Administrador");
        
        panelBtns.setLayout(new FlowLayout(FlowLayout.CENTER));
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);
        
        initUI();
        
        btnAlta.addActionListener(e -> guardarUsuario());
    }
    
    // MODO MODIFICAR
    public AltaEmpleados(Frame parent, boolean modal, Usuario usuario, Runnable onSave) {
        super(parent, modal);
        initComponents();
        this.userEditar = usuario;
        this.onSave = onSave;
        
        //UI
        jLabel8.setForeground(Styles.fontDark);
        jLabel3.setForeground(Styles.fontDark);
        
        panelAltaEmp.setBackground(Styles.bgLight);
        panelDataEmp.setBackground(Styles.bgLight);
        
        cboEmpRol.addItem("Empleado");
        cboEmpRol.addItem("Administrador");

        
        panelBtns.setLayout(new FlowLayout(FlowLayout.CENTER));
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);
        
        initUI();
        cargarDatosUsuario();
        btnAlta.addActionListener(e -> guardarUsuario());
        
        
    }

    private void initUI(){
        
        panelBtns.setBorder(new EmptyBorder(0,70,15,0));
        Btn btnLimpiar = Btn.secondary("Limpiar");
        btnLimpiar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnCerrar);
        
        panelBtns.setBackground(Styles.bgLight);
        
        txtEmpTel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Consume the event if the character is not a digit
                }
            }
        });
        
        
        btnLimpiar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtEmpUser.setText("");
                    txtEmpPass.setText("");
                    cboEmpRol.setSelectedIndex(-1);
                    txtEmpNombre.setText("");
                    txtEmpApe.setText("");
                    txtDni.setText("");
                    txtEmpTel.setText("");
                }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
        
        txtEmpTel.addKeyListener(new NumberVerifier());
        txtDni.addKeyListener(new NumberVerifier());
    }
    
    private void cargarDatosUsuario() {
        txtEmpUser.setText(userEditar.getUsername());
        txtEmpPass.setText(userEditar.getPassword());
        cboEmpRol.setSelectedItem(userEditar.getRol());
        txtEmpNombre.setText(userEditar.getNombre());
        txtEmpApe.setText(userEditar.getApellido());
        txtDni.setText(userEditar.getDni());
        txtEmpTel.setText(userEditar.getTelefono());
    }
    
    private void guardarUsuario() {

                    String user = txtEmpUser.getText();
                    String pass = txtEmpPass.getText();
                    String rol = (String) cboEmpRol.getSelectedItem();
                    String nombre = txtEmpNombre.getText();
                    String apellido = txtEmpApe.getText();
                    String dni = txtDni.getText();
                    String tel = txtEmpTel.getText();
            
        // verificar si ya existe
        if (userEditar == null) {

            if (control.doesUsernameExist(user)) {
                JOptionPane.showMessageDialog(this, "Ese username ya existe.");
                return;
            }

        } else {

            if (!userEditar.getUsername().equals(user) && control.doesUsernameExist(user)) {
                JOptionPane.showMessageDialog(this, "Ese username ya existe.");
                return;
            }

        }

        if (userEditar == null) {
            // MODO ALTA
            control.guardarUser(user, pass, nombre, apellido, tel, rol, dni);
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
        } else {
            //MODO MODIFICAR
            control.modificarUsuario(userEditar, user, pass, nombre, apellido, tel, rol, dni);
            JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.");
        }

        onSave.run();
        dispose();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelAltaEmp = new javax.swing.JPanel();
        lblCargaEmp = new javax.swing.JLabel();
        panelDataEmp = new javax.swing.JPanel();
        txtEmpUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmpNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEmpApe = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEmpTel = new javax.swing.JTextField();
        cboEmpRol = new javax.swing.JComboBox<>();
        txtEmpPass = new javax.swing.JPasswordField();
        checkPass = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        panelBtns = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("frameAltaEmp"); // NOI18N

        panelAltaEmp.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaEmp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaEmp.setText("Carga de Empleados");

        panelDataEmp.setBackground(new java.awt.Color(250, 250, 250));

        txtEmpUser.setBackground(new java.awt.Color(240, 240, 240));
        txtEmpUser.setForeground(new java.awt.Color(51, 51, 51));
        txtEmpUser.setText("Usuario");
        txtEmpUser.setBorder(null);
        txtEmpUser.setCaretColor(new java.awt.Color(255, 255, 255));
        txtEmpUser.setPreferredSize(new java.awt.Dimension(64, 28));
        txtEmpUser.setSelectionColor(new java.awt.Color(204, 204, 255));

        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Contraseña*");

        jLabel4.setText("Nombre*");

        txtEmpNombre.setBackground(new java.awt.Color(240, 240, 240));
        txtEmpNombre.setForeground(new java.awt.Color(51, 51, 51));
        txtEmpNombre.setText("Nombre");
        txtEmpNombre.setBorder(null);
        txtEmpNombre.setPreferredSize(new java.awt.Dimension(64, 28));

        jLabel5.setText("Apellido*");

        txtEmpApe.setBackground(new java.awt.Color(240, 240, 240));
        txtEmpApe.setForeground(new java.awt.Color(51, 51, 51));
        txtEmpApe.setText("Apellido");
        txtEmpApe.setBorder(null);
        txtEmpApe.setPreferredSize(new java.awt.Dimension(64, 28));

        jLabel6.setText("Rol*");

        jLabel7.setText("Nº Teléfono");

        txtEmpTel.setBackground(new java.awt.Color(240, 240, 240));
        txtEmpTel.setForeground(new java.awt.Color(51, 51, 51));
        txtEmpTel.setText("1234567890");
        txtEmpTel.setBorder(null);
        txtEmpTel.setPreferredSize(new java.awt.Dimension(84, 28));

        cboEmpRol.setBackground(new java.awt.Color(240, 240, 240));
        cboEmpRol.setBorder(null);
        cboEmpRol.setPreferredSize(new java.awt.Dimension(72, 28));

        txtEmpPass.setBackground(new java.awt.Color(240, 240, 240));
        txtEmpPass.setForeground(new java.awt.Color(51, 51, 51));
        txtEmpPass.setText("contraseña");
        txtEmpPass.setBorder(null);
        txtEmpPass.setCaretColor(new java.awt.Color(204, 204, 204));
        txtEmpPass.setPreferredSize(new java.awt.Dimension(65, 28));
        txtEmpPass.setSelectionColor(new java.awt.Color(204, 204, 255));

        checkPass.setBackground(new java.awt.Color(250, 250, 250));
        checkPass.setText("Mostrar Contraseña");
        checkPass.setBorder(null);
        checkPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPassActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Nombre de Usuario*");

        jLabel9.setText("DNI*");

        txtDni.setBackground(new java.awt.Color(240, 240, 240));
        txtDni.setForeground(new java.awt.Color(51, 51, 51));
        txtDni.setText("12345678");
        txtDni.setBorder(null);
        txtDni.setPreferredSize(new java.awt.Dimension(84, 28));

        javax.swing.GroupLayout panelBtnsLayout = new javax.swing.GroupLayout(panelBtns);
        panelBtns.setLayout(panelBtnsLayout);
        panelBtnsLayout.setHorizontalGroup(
            panelBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );
        panelBtnsLayout.setVerticalGroup(
            panelBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelDataEmpLayout = new javax.swing.GroupLayout(panelDataEmp);
        panelDataEmp.setLayout(panelDataEmpLayout);
        panelDataEmpLayout.setHorizontalGroup(
            panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataEmpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBtns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelDataEmpLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkPass)
                    .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmpPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtEmpUser, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(cboEmpRol, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmpNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpApe, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmpTel, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(108, 108, 108))
        );
        panelDataEmpLayout.setVerticalGroup(
            panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataEmpLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDataEmpLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel3)
                        .addGap(18, 63, Short.MAX_VALUE)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cboEmpRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtEmpNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtEmpApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(58, 58, 58))
                    .addGroup(panelDataEmpLayout.createSequentialGroup()
                        .addComponent(txtEmpUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(txtEmpPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(checkPass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(panelBtns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelAltaEmpLayout = new javax.swing.GroupLayout(panelAltaEmp);
        panelAltaEmp.setLayout(panelAltaEmpLayout);
        panelAltaEmpLayout.setHorizontalGroup(
            panelAltaEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDataEmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelAltaEmpLayout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(lblCargaEmp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAltaEmpLayout.setVerticalGroup(
            panelAltaEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaEmpLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblCargaEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDataEmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAltaEmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAltaEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPassActionPerformed
          if (checkPass.isSelected()){
              txtEmpPass.setEchoChar((char) 0);
          }else{
              txtEmpPass.setEchoChar('*');
          }
    }//GEN-LAST:event_checkPassActionPerformed

    private boolean validarCampos() {
        if (txtEmpUser.getText().isEmpty() || cboEmpRol.getSelectedItem() == null || txtDni.getText().isEmpty() || 
           txtEmpNombre.getText().isEmpty() || txtEmpApe.getText().isEmpty() || txtEmpPass.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        return true; // Indicate validation success
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboEmpRol;
    private javax.swing.JCheckBox checkPass;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JPanel panelAltaEmp;
    private javax.swing.JPanel panelBtns;
    private javax.swing.JPanel panelDataEmp;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEmpApe;
    private javax.swing.JTextField txtEmpNombre;
    private javax.swing.JPasswordField txtEmpPass;
    private javax.swing.JTextField txtEmpTel;
    private javax.swing.JTextField txtEmpUser;
    // End of variables declaration//GEN-END:variables
}
