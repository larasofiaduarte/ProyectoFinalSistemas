/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.login;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.TextField;
import com.mycompany.GUI.components.PasswordField;
import com.mycompany.GUI.components.ImagePanel;
import com.mycompany.GUI.components.Button;
import com.mycompany.proyectofinal.Controladora;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author duart
 */public class Login extends JDialog {

    private TextField txtUser;
    private PasswordField txtPass;
    private JLabel lblForgotPass;
    private JCheckBox check;
    private Controladora control;
    
    public Login(Frame parent) {
        super(parent, "Iniciar Sesión", true);
        this.control = new Controladora();
        initDialog(); //Abrir el Dialog de Login
        buildUI(); //UI
        addEventListeners(); //Funcionalidad Botones
        
    }

    /** Setting del Login */
    private void initDialog() {
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
    }

    /** Construcción UI */
    private void buildUI() {
        JPanel leftPanel = buildLeftPanel();
        JPanel rightPanel = buildRightPanel();

        add(leftPanel);
        add(rightPanel);

        // no focus on open
        SwingUtilities.invokeLater(() -> getContentPane().requestFocusInWindow());
    }

    /** Panel Izq (Logo) */
    private JPanel buildLeftPanel() {
        ImagePanel panel = new ImagePanel(new ImageIcon(
            "C:\\Users\\duart\\OneDrive\\Desktop\\ProyectoFinal\\com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOT\\src\\main\\resources\\images\\7020304.jpg"
        ).getImage());

        panel.setBackground(Styles.accent);
        panel.setZoomFactor(1.0);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("HECTOR");
        JLabel logo2 = new JLabel("YAGUSZ");
        JLabel subtitle = new JLabel("P   E   L   U   Q   U   E   R   I   A");

        logo.setFont(Styles.customFontXl);
        logo2.setFont(Styles.customFontXl);
        subtitle.setFont(Styles.customFontMd);

        logo.setForeground(Styles.fontLight);
        logo2.setForeground(Styles.fontLight);
        subtitle.setForeground(Styles.fontLight);

        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo2.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(logo);
        panel.add(logo2);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitle);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /** Panel Derecho (Login Form) */
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Styles.bgLight);

        // Titulo
        JLabel loginTitle = new JLabel("Iniciar Sesión");
        loginTitle.setFont(Styles.fontTitle);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(40));
        panel.add(loginTitle);
        panel.add(Box.createVerticalStrut(30));

        // User + Password 
        panel.add(buildUserPanel());
        //panel.add(Box.createVerticalStrut(20));
        panel.add(buildPassPanel());
        //panel.add(Box.createVerticalStrut(30));

        // Btns
        int buttonHeight = 40;
        Dimension buttonSize = new Dimension(300, buttonHeight);

        Button primaryBtn = Button.primary("Ingresar");
        primaryBtn.setPreferredSize(buttonSize);
        primaryBtn.setMaximumSize(buttonSize);
        primaryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        Button secondaryBtn = Button.secondary("Registrarse");
        secondaryBtn.setPreferredSize(buttonSize);
        secondaryBtn.setMaximumSize(buttonSize);
        secondaryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(primaryBtn);
        panel.add(Box.createVerticalStrut(12));
        panel.add(secondaryBtn);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(55));

        // Action for login
        primaryBtn.addActionListener(e -> Login());

        return panel;
    }

    /*Subpanel dentro de RightPanel con textfield */
    private JPanel buildUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Styles.bgLight);

        JLabel lblUser = new JLabel("Nombre de Usuario");
        lblUser.setFont(Styles.fontLbl);
        lblUser.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(45));
        lblContainer.add(lblUser);
        lblContainer.add(Box.createHorizontalGlue());

        txtUser = new TextField(20, "Ingrese su nombre de usuario");
        txtUser.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtUser);

        userPanel.add(lblContainer);
        userPanel.add(txtContainer);

        return userPanel;
    }

    /** Subpanel dentro de RightPanel con password passfield */
    private JPanel buildPassPanel() {
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.Y_AXIS));
        passPanel.setBackground(Styles.bgLight);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(Styles.fontLbl);
        lblPass.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(45));
        lblContainer.add(lblPass);
        lblContainer.add(Box.createHorizontalGlue());

        txtPass = new PasswordField(20, "Ingrese su contraseña");
        txtPass.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtPass);
        

        // restablecer pass + checkbox mostrar
        lblForgotPass = new JLabel("Contraseña olvidada");
        lblForgotPass.setFont(Styles.fontLblBold);
        lblForgotPass.setForeground(Styles.accent);

        check = new JCheckBox("Mostrar Contraseña");
        check.setBackground(Styles.bgLight);
        check.setForeground(Styles.fontPlaceholder);

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));
        bottomContainer.setBackground(Styles.bgLight);
        bottomContainer.add(Box.createHorizontalStrut(45));
        bottomContainer.add(lblForgotPass);
        bottomContainer.add(Box.createHorizontalStrut(15));
        bottomContainer.add(check);
        bottomContainer.add(Box.createHorizontalGlue());

        passPanel.add(lblContainer);
        //passPanel.add(Box.createVerticalStrut(2));
        passPanel.add(txtContainer);
        passPanel.add(bottomContainer);

        return passPanel;
    }

    /** Funcionalidad Botones */
    private void addEventListeners() {
        // checkbox
        check.addActionListener(e -> {
            if (check.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('•');
            }
        });

        // hover effect
        lblForgotPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblForgotPass.setForeground(Styles.accentHover);
                lblForgotPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblForgotPass.setForeground(Styles.accent);
            }
        });
        
    }
    
    private boolean loginExitoso = false;
    
    
    //Login logic
    private void Login() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());


        if (control != null && control.validarUsuario(user, pass)) {
            loginExitoso = true; 
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    
    }
    
    public boolean isLoginExitoso() {
        return loginExitoso;
    }
    
    //Reestablecer contraseña - abrir Ventana o dialog nueva
    
    //Registrarse -- abrir form de alta de usuario.

    
}
