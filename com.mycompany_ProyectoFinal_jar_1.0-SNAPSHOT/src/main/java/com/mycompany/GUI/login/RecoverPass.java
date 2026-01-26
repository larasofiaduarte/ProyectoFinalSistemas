/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.login;

import com.mycompany.proyectofinal.Controladora;
import javax.swing.*;
import java.awt.*;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.*;
import com.mycompany.GUI.components.Button;
import com.mycompany.GUI.components.TxtField;
import com.mycompany.GUI.components.PassField;
import com.mycompany.proyectofinal.Usuario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author duart
 */
public class RecoverPass extends JDialog {
    
    private Controladora control;
    private JPanel content;
    private TxtField txtUser;
    private PassField txtPass;
    private JCheckBox check;
    private Button primaryBtn;
    private Button secondaryBtn;
    
    public RecoverPass(Window parent){
        super(parent, "Recuperar Contraseña", ModalityType.APPLICATION_MODAL);
        this.control = new Controladora();
        initDialog(); //Abrir el Dialog de Login
        AddEventListeners();
    }
    
    
    /** Setting del Login */
    private void initDialog() {
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Styles.bgLight);

        setContentPane(content);
        
        buildUI();
    }
    
    private void buildUI(){
        // Titulo
        JLabel panelTitle = new JLabel("Recuperar Contraseña");
        panelTitle.setFont(Styles.fontTitle);
        panelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.add(panelTitle);
        
        JPanel userPanel = buildUserPanel();
        JPanel passPanel = buildPassPanel();  
        JPanel btnPanel = buildBtnPanel();
        
        content.add(userPanel);
        content.add(passPanel);
        content.add(btnPanel);
    
    }
    
    private JPanel buildUserPanel(){
        
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Styles.bgLight);
        
        userPanel.add(Box.createVerticalStrut(45));

        JLabel lblUser = new JLabel("Nombre de Usuario");
        lblUser.setFont(Styles.fontLbl);
        lblUser.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(25));
        lblContainer.add(lblUser);
        lblContainer.add(Box.createHorizontalGlue());
        
        lblContainer.add(Box.createVerticalStrut(10));

        txtUser = new com.mycompany.GUI.components.TxtField(20, "Ingrese su nombre de usuario");
        txtUser.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtUser);

        userPanel.add(lblContainer);
        userPanel.add(txtContainer);

        return userPanel;
        
    }
    
    private JPanel buildPassPanel() {
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.Y_AXIS));
        passPanel.setBackground(Styles.bgLight);

        JLabel lblPass = new JLabel("Nueva Contraseña");
        lblPass.setFont(Styles.fontLbl);
        lblPass.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(25));
        
        lblContainer.add(lblPass);
        lblContainer.add(Box.createHorizontalGlue());

        txtPass = new PassField(20, "Ingrese su nueva contraseña");
        txtPass.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtPass);
        
        //check
        check = new JCheckBox("Mostrar Contraseña");
        check.setBackground(Styles.bgLight);
        check.setForeground(Styles.fontPlaceholder);

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));
        bottomContainer.setBackground(Styles.bgLight);
        
        bottomContainer.add(Box.createHorizontalStrut(15));
        bottomContainer.add(check);
        bottomContainer.add(Box.createHorizontalGlue());

        passPanel.add(lblContainer);
        passPanel.add(txtContainer);
        passPanel.add(bottomContainer);

        return passPanel;
    }
    
    //panel botones
    private JPanel buildBtnPanel(){
        JPanel panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Styles.bgLight);
        
        int buttonHeight = 40;
        Dimension buttonSize = new Dimension(300, buttonHeight);
        
        primaryBtn = com.mycompany.GUI.components.Button.primary("Aceptar");
        primaryBtn.setPreferredSize(buttonSize);
        primaryBtn.setMaximumSize(buttonSize);

        secondaryBtn = com.mycompany.GUI.components.Button.secondary("Cancelar");
        secondaryBtn.setPreferredSize(buttonSize);
        secondaryBtn.setMaximumSize(buttonSize);
        
        panel.add(secondaryBtn);
        panel.add(primaryBtn);
        
        return panel;
    
    }
    
    private void AddEventListeners(){
        //Reset Pass
        primaryBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String user = txtUser.getText();
                String newPass = new String(txtPass.getPassword());
                
                //buscar el nombre de usuario ingresado en la variable user para ver si existe en la BD
                //si existe, cambiar la contrasenia actual del objeto user en la BD por newPass
                
                // verificar si existe el user ingresado
                if (user.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(
                    RecoverPass.this,
                    "Complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (!control.doesUsernameExist(user)) {
                    JOptionPane.showMessageDialog(
                        RecoverPass.this,
                        "El usuario no existe",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                control.resetPassword(user, newPass);

                JOptionPane.showMessageDialog(
                    RecoverPass.this,
                    "Contraseña actualizada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                dispose();
                
            }
        });
        
        
        //Cancelar
        secondaryBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        
        // checkbox
        check.addActionListener(e -> {
            if (check.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('•');
            }
        });
        
    }
    
    
    
}
