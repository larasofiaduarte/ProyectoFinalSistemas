package com.mycompany.GUI.components;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.login.Login;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Session;
import com.mycompany.persistencia.NotificationService;
import com.formdev.flatlaf.extras.FlatSVGIcon;


public class TopMenu extends JPanel {

    private JButton icon;
    private JPopupMenu menuUsuario;
    private JMenuItem itemCerrarSesion;

    private JButton notifButton;
    private JPopupMenu menuNotificaciones;

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
            login.setVisible(true);
        });

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
