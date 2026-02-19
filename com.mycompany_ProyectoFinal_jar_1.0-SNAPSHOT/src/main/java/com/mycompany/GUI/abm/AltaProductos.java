
package com.mycompany.GUI.abm;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Proveedor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;


public class AltaProductos extends javax.swing.JFrame {
    
    Controladora control = new Controladora();

    public AltaProductos() {
        initComponents();
        
        Btn btnAlta = Btn.primary("Guardar");
        panelBtns.add(btnAlta);
        
        Btn btnLimpiar = Btn.secondary("Limpiar");
        panelBtns.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        panelBtns.add(btnCerrar);
        
        txtStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Consume the event if the character is not a digit
                }
            }
        });
        
        txtMinimo.addKeyListener(new KeyAdapter() {
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
                    txtNombre.setText("");
                    txtStock.setText("");
                    txtMinimo.setText("");
                    txtProveedor.setText("");
                }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
        
        txtProveedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Consume the event if the character is not a digit
                }
            }
        });
        
        
        btnAlta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nombre = txtNombre.getText();
                    Double stock = Double.parseDouble(txtStock.getText());;
                    String minimo = txtMinimo.getText();
                    String prov = txtProveedor.getText();
                    int idProveedor = Integer.parseInt(txtProveedor.getText()); 
                    
                    Proveedor proveedor = control.findProveedor(idProveedor);
                    
                    
                    if (validarCampos()){
                        if (proveedor == null) {
                            JOptionPane.showMessageDialog(null, "Proveedor no encontrado. Por favor, ingrese un ID de proveedor válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit the action if prov does not exist
                        }else{
                            control.guardarProducto(nombre, stock, minimo, proveedor);
                            JOptionPane.showMessageDialog(null, "Producto guardado correctamente.", "Producto guardado.", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        
                    }

                }
        });
        
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
        txtProveedor = new javax.swing.JTextField();
        panelBtns = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaEmp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaEmp.setText("Carga de Productos");

        jPanel2.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Nombre*");

        jLabel2.setText("Stock Actual*");

        jLabel3.setText("Stock Minimo*");

        jLabel4.setText("ID Proveedor*");

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

        txtProveedor.setBackground(new java.awt.Color(242, 242, 242));
        txtProveedor.setForeground(new java.awt.Color(102, 102, 102));
        txtProveedor.setText("1");
        txtProveedor.setBorder(null);
        txtProveedor.setPreferredSize(new java.awt.Dimension(73, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(txtStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(79, Short.MAX_VALUE))
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
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(88, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblCargaEmp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBtns, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
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

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AltaProductos().setVisible(true);
            }
        });
    }
    
    private boolean validarCampos() {
        if (txtProveedor.getText().isEmpty() ||
                txtMinimo.getText().isEmpty() ||
                txtStock.getText().isEmpty()||
                txtNombre.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        return true; // Indicate validation success
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JPanel panelBtns;
    private javax.swing.JTextField txtMinimo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtProveedor;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
