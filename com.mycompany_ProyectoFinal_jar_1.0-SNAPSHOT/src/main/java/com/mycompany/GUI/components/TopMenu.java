package com.mycompany.GUI.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.login.Login;
import com.mycompany.proyectofinal.HorarioConfig;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.ProyectoFinal;
import com.mycompany.proyectofinal.Session;
import com.mycompany.proyectofinal.Usuario;
import java.time.LocalTime;
import com.mycompany.persistencia.NotificationService;
import com.formdev.flatlaf.extras.FlatSVGIcon;


public class TopMenu extends JPanel {

    private JButton icon;
    private JPopupMenu menuUsuario;
    private JMenuItem itemCerrarSesion;

    private JButton notifButton;
    private JPopupMenu menuNotificaciones;

    private JButton settingsButton;
    private FlatSVGIcon settingsIcon;
    private JPopupMenu menuSettings;
    private JMenuItem itemHorarios;
    private JMenuItem itemAppLog;

    private FlatSVGIcon bell;
    private FlatSVGIcon dot;

    private NotificationService notificationService;

    public TopMenu() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBackground(Styles.bgDark);

        // --- Notification button ---
        notifButton = new JButton();
        notifButton.setContentAreaFilled(false);
        notifButton.setBorderPainted(false);
        notifButton.setFocusPainted(false);
        notifButton.setOpaque(false);

        bell = new FlatSVGIcon("images/bell.svg", 20, 20);
        bell.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));

        dot = new FlatSVGIcon("images/dot.svg", 34, 34);
        dot.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.RED));

        menuNotificaciones = new JPopupMenu();

        notifButton.addActionListener(e -> {
            cargarNotificaciones();
            menuNotificaciones.show(notifButton, 0, notifButton.getHeight());
        });

        // --- Settings button ---
        settingsButton = new JButton();
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setOpaque(false);

        settingsIcon = new FlatSVGIcon("images/settings.svg", 22, 22);
        settingsIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));
        settingsButton.setIcon(settingsIcon);

        // --- Settings dropdown ---
        menuSettings = new JPopupMenu();

        // Horarios de apertura: Empleado no debe verlo ni acceder — solo se agrega al menú
        // (ni siquiera existe) para Administrador/Dueño, igual criterio que el resto de
        // restricciones por rol (Session.tieneAccesoCompleto()).
        if (Session.tieneAccesoCompleto()) {
            itemHorarios = new JMenuItem("Horarios de apertura");
            menuSettings.add(itemHorarios);

            itemHorarios.addActionListener(e -> {
                if (!Session.tieneAccesoCompleto()) {
                    JOptionPane.showMessageDialog(this,
                        "Solamente el administrador puede acceder a los horarios de apertura.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Window parent = SwingUtilities.getWindowAncestor(this);
                HorariosDialog dialog = new HorariosDialog(parent);
                dialog.setVisible(true);
                if (dialog.isGuardado()) {
                    java.util.List<LocalTime[]> ivs = dialog.getIntervalos();
                    HorarioConfig.setIntervalos(ivs);
                    String msg;
                    if (ivs.size() == 1) {
                        msg = "Horario: " + ivs.get(0)[0] + " - " + ivs.get(0)[1];
                    } else {
                        msg = "Mañana: " + ivs.get(0)[0] + " - " + ivs.get(0)[1]
                            + "\nTarde: "  + ivs.get(1)[0] + " - " + ivs.get(1)[1];
                    }
                    JOptionPane.showMessageDialog(parent,
                        "Horario guardado correctamente.\n" + msg,
                        "Horarios de apertura", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        // "App Log" solo se agrega al menú (ni siquiera existe para otros roles) si el usuario
        // logueado es Administrador — mismo criterio de rol que Usuarios.eliminarUser() /
        // Conceptos.eliminarConcepto().
        Usuario currentUser = Session.getCurrentUser();
        if (currentUser != null && currentUser.getRol().equalsIgnoreCase("Administrador")) {
            itemAppLog = new JMenuItem("App Log");
            menuSettings.add(itemAppLog);

            itemAppLog.addActionListener(e -> {
                // Respaldo por si el rol cambió luego de construir el menú (ej. sesión reutilizada).
                Usuario u = Session.getCurrentUser();
                if (u == null || !u.getRol().equalsIgnoreCase("Administrador")) {
                    JOptionPane.showMessageDialog(this,
                        "Solamente el administrador puede ver los logs del sistema.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Window parent = SwingUtilities.getWindowAncestor(this);
                AppLogDialog dialog = new AppLogDialog(parent);
                dialog.setVisible(true);
            });
        }

        settingsButton.addActionListener(e ->
            menuSettings.show(settingsButton, 0, settingsButton.getHeight())
        );

        // --- User icon button ---
        icon = new JButton("");
        icon.setContentAreaFilled(false);
        icon.setBorderPainted(false);
        icon.setFocusPainted(false);
        icon.setOpaque(false);

        FlatSVGIcon userIcon = new FlatSVGIcon("images/user.svg", 24, 24);
        userIcon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.WHITE));
        icon.setIcon(userIcon);

        // --- User dropdown ---
        menuUsuario = new JPopupMenu();
        itemCerrarSesion = new JMenuItem("Cerrar sesión");
        menuUsuario.add(itemCerrarSesion);

        icon.addActionListener(e -> menuUsuario.show(icon, 0, icon.getHeight()));

        itemCerrarSesion.addActionListener(e -> {
            Session.setCurrentUser(null);
            SwingUtilities.getWindowAncestor(this).dispose();
            Login login = new Login(null);
            login.setVisible(true); // modal → blocks

            // Antes esto no chequeaba isLoginExitoso(): si el re-login era correcto, la app se
            // quedaba sin ninguna ventana visible (mismo bug que main() ya tenía). Reusa el mismo
            // camino que el arranque normal para que ambos se comporten igual.
            if (login.isLoginExitoso()) {
                ProyectoFinal.abrirVentanaPrincipal();
            } else {
                System.exit(0);
            }
        });

        add(settingsButton);
        add(notifButton);
        add(icon);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // --- Subscribe to singleton NotificationService (event-driven, no polling) ---
        notificationService = NotificationService.getInstance();
        notificationService.setOnChangeListener(() ->
            SwingUtilities.invokeLater(this::actualizarNotificacionIcono)
        );

        actualizarNotificacionIcono();
    }

    private void actualizarNotificacionIcono() {
        if (notificationService.hasNotifications()) {
            notifButton.setIcon(new ActiveNotif(bell, dot));
        } else {
            notifButton.setIcon(bell);
        }
    }

    private void cargarNotificaciones() {
        menuNotificaciones.removeAll();

        List<Producto> bajos = notificationService.getCachedLowStock();

        if (bajos.isEmpty()) {
            JMenuItem empty = new JMenuItem("Sin notificaciones");
            empty.setEnabled(false);
            menuNotificaciones.add(empty);
            return;
        }

        for (Producto p : bajos) {
            JMenuItem item = new JMenuItem(
                " ! Stock Bajo: " + p.getNombre() + " (Stock: " + p.getStock() + ")"
            );
            item.addActionListener(e -> {
                Ventana ventana = (Ventana) SwingUtilities.getAncestorOfClass(Ventana.class, notifButton);
                if (ventana != null) {
                    ventana.showCard("INVENTARIO");
                    ventana.seleccionarProductoEnInventario(p);
                }
            });
            menuNotificaciones.add(item);
        }
    }
}
