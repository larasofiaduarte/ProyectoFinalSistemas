package com.mycompany.GUI.login;

import com.mycompany.proyectofinal.Controladora;
import javax.swing.*;
import java.awt.*;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.*;
import com.mycompany.GUI.components.Button;

public class RecoverPass extends JDialog {

    private Controladora control;

    private TxtField txtUser;
    private TxtField txtDni;
    private PassField txtPass;
    private PassField txtConfirmPass;
    private JCheckBox check;

    private Button primaryBtnStep1;
    private Button secondaryBtnStep1;

    private Button primaryBtnStep2;
    private Button secondaryBtnStep2;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JPanel step1;
    private JPanel step2;

    public RecoverPass(Window parent) {
        super(parent, "Recuperar Contraseña", ModalityType.APPLICATION_MODAL);
        this.control = new Controladora();
        initDialog();
        addEventListeners();
    }

    // ================= DIALOG =================

    private void initDialog() {
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Styles.bgLight);

        step1 = buildStep1();
        step2 = buildStep2();

        cardPanel.add(step1, "STEP1");
        cardPanel.add(step2, "STEP2");

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "STEP1");
    }

    // ================= STEP 1 =================

    private JPanel buildStep1() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Styles.bgLight);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel panelTitle = new JLabel("Recuperar Contraseña");
        panelTitle.setFont(Styles.fontTitle);
        panelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(panelTitle);
        panel.add(buildUserPanel());
        panel.add(buildDniPanel());
        panel.add(Box.createVerticalStrut(15));
        panel.add(buildBtnPanelStep1());

        return panel;
    }

    // ================= STEP 2 =================

    private JPanel buildStep2() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Styles.bgLight);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel panelTitle = new JLabel("Nueva Contraseña");
        panelTitle.setFont(Styles.fontTitle);
        panelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(panelTitle);
        panel.add(buildPassPanel());
        panel.add(buildConfirmPassPanel());
        
        panel.add(Box.createHorizontalStrut(15));
        panel.add(buildCheckPanel());
        
        panel.add(buildBtnPanelStep2());

        return panel;
    }

    // ================= USER =================

    private JPanel buildUserPanel() {

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Styles.bgLight);

        userPanel.add(Box.createVerticalStrut(30));

        JLabel lblUser = new JLabel("Nombre de Usuario");
        lblUser.setFont(Styles.fontLbl);
        lblUser.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(25));
        lblContainer.add(lblUser);
        lblContainer.add(Box.createHorizontalGlue());

        txtUser = new TxtField(20, "Ingrese su nombre de usuario");
        txtUser.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtUser);

        userPanel.add(lblContainer);
        userPanel.add(txtContainer);

        return userPanel;
    }

    // ================= DNI =================

    private JPanel buildDniPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Styles.bgLight);

        JLabel lbl = new JLabel("DNI");
        lbl.setFont(Styles.fontLbl);
        lbl.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(25));
        lblContainer.add(lbl);
        lblContainer.add(Box.createHorizontalGlue());

        txtDni = new TxtField(20, "Ingrese su DNI");
        txtDni.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtDni);

        panel.add(lblContainer);
        panel.add(txtContainer);

        return panel;
    }

    // ================= PASSWORD =================

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
        
        

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));
        bottomContainer.setBackground(Styles.bgLight);
        bottomContainer.add(Box.createHorizontalStrut(15));
        bottomContainer.add(Box.createHorizontalGlue());

        passPanel.add(lblContainer);
        passPanel.add(txtContainer);
        passPanel.add(bottomContainer);

        return passPanel;
    }

    private JPanel buildConfirmPassPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Styles.bgLight);

        JLabel lbl = new JLabel("Confirmar Contraseña");
        lbl.setFont(Styles.fontLbl);
        lbl.setForeground(Styles.fontDark);

        JPanel lblContainer = new JPanel();
        lblContainer.setLayout(new BoxLayout(lblContainer, BoxLayout.X_AXIS));
        lblContainer.setBackground(Styles.bgLight);
        lblContainer.add(Box.createHorizontalStrut(25));
        lblContainer.add(lbl);
        lblContainer.add(Box.createHorizontalGlue());

        txtConfirmPass = new PassField(20, "Repita la contraseña");
        txtConfirmPass.setMaximumSize(new Dimension(300, 40));

        JPanel txtContainer = new JPanel();
        txtContainer.setLayout(new BoxLayout(txtContainer, BoxLayout.X_AXIS));
        txtContainer.setBackground(Styles.bgLight);
        txtContainer.add(txtConfirmPass);

        panel.add(lblContainer);
        panel.add(txtContainer);

        return panel;
    }

    // ================= BUTTON PANELS =================

    private JPanel buildBtnPanelStep1() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Styles.bgLight);

        Dimension buttonSize = new Dimension(300, 40);

        secondaryBtnStep1 = Button.secondary("Cancelar");
        secondaryBtnStep1.setPreferredSize(Styles.btnSizeSm);
        secondaryBtnStep1.setMaximumSize(Styles.btnSizeSm);
        secondaryBtnStep1.setMinimumSize(Styles.btnSizeSm);
        


        primaryBtnStep1 = Button.primary("Continuar");
        primaryBtnStep1.setPreferredSize(Styles.btnSizeSm);
        primaryBtnStep1.setMaximumSize(Styles.btnSizeSm);
        primaryBtnStep1.setMinimumSize(Styles.btnSizeSm);

        panel.add(secondaryBtnStep1);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(primaryBtnStep1);

        return panel;
    }
    
    private JPanel buildCheckPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        panel.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
        check = new JCheckBox("Mostrar Contraseña");
        check.setBackground(Styles.bgLight);
        check.setForeground(Styles.fontPlaceholder);
        panel.add(check);
        return panel;
    }
    
    private JPanel buildBtnPanelStep2() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Styles.bgLight);

        Dimension buttonSize = new Dimension(300, 40);
        
        
        
        secondaryBtnStep2 = Button.secondary("Cancelar");
        secondaryBtnStep2.setPreferredSize(Styles.btnSizeSm);
        secondaryBtnStep2.setMaximumSize(Styles.btnSizeSm);
        secondaryBtnStep2.setMinimumSize(Styles.btnSizeSm);
        
        primaryBtnStep2 = Button.primary("Aceptar");
        primaryBtnStep2.setPreferredSize(Styles.btnSizeSm);
        primaryBtnStep2.setMaximumSize(Styles.btnSizeSm);
        primaryBtnStep2.setMinimumSize(Styles.btnSizeSm);
        
        panel.add(secondaryBtnStep2);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(primaryBtnStep2);

        return panel;
    }

    // ================= EVENTS =================

    private void addEventListeners() {

        // STEP 1 → STEP 2
        primaryBtnStep1.addActionListener(e -> {

            String user = txtUser.getText();
            String dni = txtDni.getText();

            if (user.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            if (!control.validarUsuarioYDni(user, dni)) {
                JOptionPane.showMessageDialog(this, "Usuario y DNI no coinciden");
                return;
            }

            cardLayout.show(cardPanel, "STEP2");
        });

        secondaryBtnStep1.addActionListener(e -> dispose());

        // STEP 2 → GUARDAR PASS
        primaryBtnStep2.addActionListener(e -> {

            String pass1 = new String(txtPass.getPassword());
            String pass2 = new String(txtConfirmPass.getPassword());

            if (pass1.isEmpty() || pass2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete ambos campos");
                return;
            }

            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
                return;
            }

            control.resetPassword(txtUser.getText(), pass1);

            JOptionPane.showMessageDialog(this, "Contraseña actualizada");
            dispose();
        });

        secondaryBtnStep2.addActionListener(e -> dispose());
        
        
        check.addActionListener(e -> {
            txtPass.setEchoChar(check.isSelected() ? (char) 0 : '•');
            txtConfirmPass.setEchoChar(check.isSelected() ? (char) 0 : '•');
        });


    }
}

