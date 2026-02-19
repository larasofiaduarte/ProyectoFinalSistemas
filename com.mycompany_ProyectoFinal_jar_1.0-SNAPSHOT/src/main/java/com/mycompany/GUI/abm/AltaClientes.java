
package com.mycompany.GUI.abm;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.Controladora;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;


public class AltaClientes extends JDialog {
    
    Controladora control = new Controladora();
    private Runnable onSave;


    public AltaClientes(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;

        initComponents();
        
        //UI
        
        Btn btnAlta = Btn.primary("Guardar");
        panelBtns.add(btnAlta);
        
        Btn btnLimpiar = Btn.secondary("Limpiar");
        panelBtns.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        panelBtns.add(btnCerrar);
        
        txtTelCli.addKeyListener(new KeyAdapter() {
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
                    txtNombreCli.setText("");
                    txtApellidoCli.setText("");
                    txtTelCli.setText("");
                }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
        
        
        btnAlta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nombre = txtNombreCli.getText();
                    String apellido = txtApellidoCli.getText();
                    String tel = txtTelCli.getText();
                    String genero = "";
                    
                    if (RadioBtnF.isSelected()){
                        genero = "F";
                    }else if(RadioBtnM.isSelected()){
                        genero = "M";
                    }
                    
                    if (validarCampos()){
                        control.guardarCliente(nombre, apellido, tel, genero);
                        JOptionPane.showMessageDialog(null, "Cliente guardado correctamente.", "Cliente guardado.", JOptionPane.INFORMATION_MESSAGE);
                        if (onSave != null) {
                            onSave.run();   // 👈 refresh table
                        }
                        dispose();
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
        txtApellidoCli = new javax.swing.JTextField();
        txtNombreCli = new javax.swing.JTextField();
        txtTelCli = new javax.swing.JTextField();
        RadioBtnF = new javax.swing.JRadioButton();
        RadioBtnM = new javax.swing.JRadioButton();
        panelBtns = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaEmp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaEmp.setText("Carga de Clientes");

        jPanel2.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Nombre*");

        jLabel2.setText("Apellido*");

        jLabel3.setText("Teléfono");

        jLabel4.setText("Género");

        txtApellidoCli.setBackground(new java.awt.Color(242, 242, 242));
        txtApellidoCli.setForeground(new java.awt.Color(102, 102, 102));
        txtApellidoCli.setText("Apellido");
        txtApellidoCli.setBorder(null);
        txtApellidoCli.setPreferredSize(new java.awt.Dimension(73, 30));

        txtNombreCli.setBackground(new java.awt.Color(242, 242, 242));
        txtNombreCli.setForeground(new java.awt.Color(102, 102, 102));
        txtNombreCli.setText("Nombre");
        txtNombreCli.setBorder(null);
        txtNombreCli.setPreferredSize(new java.awt.Dimension(73, 30));

        txtTelCli.setBackground(new java.awt.Color(242, 242, 242));
        txtTelCli.setForeground(new java.awt.Color(102, 102, 102));
        txtTelCli.setText("1234567890");
        txtTelCli.setBorder(null);
        txtTelCli.setPreferredSize(new java.awt.Dimension(73, 30));

        RadioBtnF.setBackground(new java.awt.Color(250, 250, 250));
        RadioBtnF.setText("F");

        RadioBtnM.setBackground(new java.awt.Color(250, 250, 250));
        RadioBtnM.setText("M");

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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(RadioBtnF, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioBtnM, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNombreCli, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(txtApellidoCli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTelCli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombreCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtApellidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTelCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(RadioBtnF)
                    .addComponent(RadioBtnM))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        panelBtns.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelBtns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addComponent(lblCargaEmp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblCargaEmp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBtns, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        if (txtNombreCli.getText().isEmpty() || 
            txtApellidoCli.getText().isEmpty() ) {

            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false; // Indicate validation failure
        }
        return true; // Indicate validation success
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RadioBtnF;
    private javax.swing.JRadioButton RadioBtnM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCargaEmp;
    private javax.swing.JPanel panelBtns;
    private javax.swing.JTextField txtApellidoCli;
    private javax.swing.JTextField txtNombreCli;
    private javax.swing.JTextField txtTelCli;
    // End of variables declaration//GEN-END:variables
}
