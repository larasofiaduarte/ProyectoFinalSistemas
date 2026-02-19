
package com.mycompany.GUI.abm;

import com.mycompany.proyectofinal.Controladora;
import java.awt.Font;
import com.mycompany.GUI.Styles;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class AltaEmpleados extends javax.swing.JFrame {

    Controladora control = new Controladora();
    
    public AltaEmpleados() {
        initComponents();
        
        panelAltaEmp.setBackground(Styles.bgLight);
        panelDataEmp.setBackground(Styles.bgLight);
        
        cboEmpRol.addItem("Empleado");
        cboEmpRol.addItem("Administrador");
        
        //btnAltaEmp.setBackground(Styles.accentDark);
        btnAltaEmp.setContentAreaFilled(false);
        btnAltaEmp.setBorderPainted(false);
        btnAltaEmp.setOpaque(true);
        btnAltaEmp.setFont(new Font("Dialog", Font.BOLD, 14));
        
        btnLimpiarEmp.setBackground(Styles.btnSec);
        btnLimpiarEmp.setContentAreaFilled(false);
        btnLimpiarEmp.setBorderPainted(true);
        btnLimpiarEmp.setOpaque(true);
        btnLimpiarEmp.setFont(Styles.fontBtn);
        btnLimpiarEmp.setForeground(Styles.fontDark);
        
        btnCerrarEmp.setBackground(Styles.btnSec);
        btnCerrarEmp.setContentAreaFilled(false);
        btnCerrarEmp.setBorderPainted(true);
        btnCerrarEmp.setOpaque(true);
        btnCerrarEmp.setFont(Styles.fontBtn);
        btnCerrarEmp.setForeground(Styles.fontDark);
        
        txtEmpTel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Consume the event if the character is not a digit
                }
            }
        });
    }

    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelAltaEmp = new javax.swing.JPanel();
        lblCargaEmp = new javax.swing.JLabel();
        panelDataEmp = new javax.swing.JPanel();
        txtEmpUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnAltaEmp = new javax.swing.JButton();
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
        btnCerrarEmp = new javax.swing.JButton();
        btnLimpiarEmp = new javax.swing.JButton();

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

        btnAltaEmp.setBackground(new java.awt.Color(112, 80, 175));
        btnAltaEmp.setForeground(new java.awt.Color(255, 255, 255));
        btnAltaEmp.setText("Guardar");
        btnAltaEmp.setName("btnAltaEmp"); // NOI18N
        btnAltaEmp.setPreferredSize(new java.awt.Dimension(72, 20));
        btnAltaEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAltaEmpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAltaEmpMouseExited(evt);
            }
        });
        btnAltaEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaEmpActionPerformed(evt);
            }
        });

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

        btnCerrarEmp.setForeground(new java.awt.Color(51, 51, 51));
        btnCerrarEmp.setText("Cerrar");
        btnCerrarEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btnCerrarEmp.setName("btnAltaEmp"); // NOI18N
        btnCerrarEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarEmpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarEmpMouseExited(evt);
            }
        });
        btnCerrarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarEmpActionPerformed(evt);
            }
        });

        btnLimpiarEmp.setForeground(new java.awt.Color(51, 51, 51));
        btnLimpiarEmp.setText("Limpiar");
        btnLimpiarEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        btnLimpiarEmp.setName("btnAltaEmp"); // NOI18N
        btnLimpiarEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimpiarEmpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimpiarEmpMouseExited(evt);
            }
        });
        btnLimpiarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarEmpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDataEmpLayout = new javax.swing.GroupLayout(panelDataEmp);
        panelDataEmp.setLayout(panelDataEmpLayout);
        panelDataEmpLayout.setHorizontalGroup(
            panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataEmpLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDataEmpLayout.createSequentialGroup()
                        .addComponent(btnAltaEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiarEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                        .addComponent(btnCerrarEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(panelDataEmpLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDataEmpLayout.createSequentialGroup()
                                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboEmpRol, javax.swing.GroupLayout.Alignment.TRAILING, 0, 260, Short.MAX_VALUE)
                                    .addComponent(txtEmpUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtEmpPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(panelDataEmpLayout.createSequentialGroup()
                                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmpNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmpApe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmpTel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkPass)
                        .addGap(46, 46, 46))))
        );
        panelDataEmpLayout.setVerticalGroup(
            panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataEmpLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDataEmpLayout.createSequentialGroup()
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtEmpPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkPass))
                        .addGap(43, 43, 43)
                        .addComponent(jLabel6))
                    .addComponent(cboEmpRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEmpNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEmpApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtEmpTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(panelDataEmpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrarEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAltaEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
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

    private void btnAltaEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaEmpActionPerformed
        String user = txtEmpUser.getText();
        String pass = txtEmpPass.getText();
        String nombre = txtEmpNombre.getText();
        String apellido = txtEmpApe.getText();
        String tel = txtEmpTel.getText();
        
        String rol = (String) cboEmpRol.getSelectedItem();
        
        if (control.doesUsernameExist(user)) {
            // If it exists, show an error message
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            
            if(validarCampos()){
                control.guardar(user, pass, nombre, apellido, tel, rol);
                JOptionPane.showMessageDialog(null, "Usuario creado correctamente.", "Usuario guardado.", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            
        }
        
    }//GEN-LAST:event_btnAltaEmpActionPerformed

    private void checkPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPassActionPerformed
          if (checkPass.isSelected()){
              txtEmpPass.setEchoChar((char) 0);
          }else{
              txtEmpPass.setEchoChar('*');
          }
    }//GEN-LAST:event_checkPassActionPerformed

    private void btnCerrarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarEmpActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarEmpActionPerformed

    private void btnLimpiarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarEmpActionPerformed
        txtEmpUser.setText("");
        txtEmpPass.setText("");
        txtEmpNombre.setText("");
        txtEmpApe.setText("");
        txtEmpTel.setText("");
    }//GEN-LAST:event_btnLimpiarEmpActionPerformed

    private void btnAltaEmpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaEmpMouseEntered
        btnAltaEmp.setBackground(Styles.accentHover);
    }//GEN-LAST:event_btnAltaEmpMouseEntered

    private void btnAltaEmpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaEmpMouseExited
        btnAltaEmp.setBackground(Styles.accent);
    }//GEN-LAST:event_btnAltaEmpMouseExited

    private void btnLimpiarEmpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarEmpMouseEntered
        btnLimpiarEmp.setBackground(Styles.bgLight);
    }//GEN-LAST:event_btnLimpiarEmpMouseEntered

    private void btnLimpiarEmpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarEmpMouseExited
        btnLimpiarEmp.setBackground(Styles.bgLight);
    }//GEN-LAST:event_btnLimpiarEmpMouseExited

    private void btnCerrarEmpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarEmpMouseEntered
        btnCerrarEmp.setBackground(Styles.bgLight);
    }//GEN-LAST:event_btnCerrarEmpMouseEntered

    private void btnCerrarEmpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarEmpMouseExited
        btnCerrarEmp.setBackground(Styles.bgLight);
    }//GEN-LAST:event_btnCerrarEmpMouseExited

    private boolean validarCampos() {
        if (txtEmpUser.getText().isEmpty() || cboEmpRol.getSelectedItem() == null || 
           txtEmpNombre.getText().isEmpty() || txtEmpApe.getText().isEmpty() || txtEmpPass.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        return true; // Indicate validation success
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltaEmp;
    private javax.swing.JButton btnCerrarEmp;
    private javax.swing.JButton btnLimpiarEmp;
    private javax.swing.JComboBox<String> cboEmpRol;
    private javax.swing.JCheckBox checkPass;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JPanel panelAltaEmp;
    private javax.swing.JPanel panelDataEmp;
    private javax.swing.JTextField txtEmpApe;
    private javax.swing.JTextField txtEmpNombre;
    private javax.swing.JPasswordField txtEmpPass;
    private javax.swing.JTextField txtEmpTel;
    private javax.swing.JTextField txtEmpUser;
    // End of variables declaration//GEN-END:variables
}
