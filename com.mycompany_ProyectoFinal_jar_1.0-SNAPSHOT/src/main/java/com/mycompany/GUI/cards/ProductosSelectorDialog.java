package com.mycompany.GUI.cards;

import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.AltaProductos;
import com.mycompany.proyectofinal.*;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class ProductosSelectorDialog extends JDialog {

    private final Servicio servicio;
    private List<Producto> allProductos;
    private final Controladora control;
    private final Ventana ventana;

    private JPanel checkPanel;
    private final List<JCheckBox> checkBoxes = new ArrayList<>();
    private boolean firstBuild = true;
    private boolean saved = false;

    public ProductosSelectorDialog(Frame parent, Servicio servicio, List<Producto> allProductos,
                                   Controladora control, Ventana ventana) {
        super(parent, "Seleccionar Productos", true);
        this.servicio = servicio;
        this.allProductos = new ArrayList<>(allProductos);
        this.control = control;
        this.ventana = ventana;
        initUI();
        pack();
        setMinimumSize(new Dimension(280, 300));
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buildCheckboxes();

        JScrollPane scroll = new JScrollPane(checkPanel);
        scroll.setPreferredSize(new Dimension(260, 220));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo Producto");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnNuevo.addActionListener(e -> {
            AltaProductos d = new AltaProductos((Frame) getOwner(), true, () -> {});
            d.setLocationRelativeTo(this);
            d.setVisible(true);
            allProductos = control.traerProductos();
            buildCheckboxes();
        });

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnNuevo);
        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void buildCheckboxes() {
        java.util.Set<String> checked = new java.util.HashSet<>();

        if (firstBuild) {
            if (servicio.getProductos() != null) {
                for (ServicioProducto sp : servicio.getProductos()) {
                    if (sp.getProducto() != null) {
                        checked.add(sp.getProducto().getNombre());
                    }
                }
            }
            firstBuild = false;
        } else {
            for (JCheckBox cb : checkBoxes) {
                if (cb.isSelected()) checked.add(cb.getText());
            }
        }

        checkPanel.removeAll();
        checkBoxes.clear();

        for (Producto p : allProductos) {
            JCheckBox cb = new JCheckBox(p.getNombre());
            cb.setSelected(checked.contains(p.getNombre()));
            checkBoxes.add(cb);
            checkPanel.add(cb);
        }

        checkPanel.revalidate();
        checkPanel.repaint();
    }

    private void guardar() {
        String oldValue = servicio.getProductos() == null ? "" :
            servicio.getProductos().stream()
                .filter(sp -> sp.getProducto() != null)
                .map(sp -> sp.getProducto().getNombre())
                .sorted()
                .collect(Collectors.joining(", "));

        for (ServicioProducto sp : new java.util.ArrayList<>(servicio.getProductos())) {
            servicio.removeProducto(sp);
        }

        for (int i = 0; i < allProductos.size() && i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                Producto p = allProductos.get(i);
                ServicioProducto sp = new ServicioProducto();
                sp.setServicio(servicio);
                sp.setProducto(p);
                sp.setCantidadUsada(1);
                servicio.addProducto(sp);
            }
        }

        control.modificarServicio(servicio);

        String newValue = servicio.getProductos().stream()
            .filter(sp -> sp.getProducto() != null)
            .map(sp -> sp.getProducto().getNombre())
            .sorted()
            .collect(Collectors.joining(", "));

        if (!oldValue.equals(newValue)) {
            RegistrarActividad.registrar(
                "SERVICIOS",
                String.valueOf(servicio.getId()),
                "productos",
                oldValue,
                newValue,
                "MODIFICACION"
            );
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
