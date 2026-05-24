package com.mycompany.GUI.cards;

import com.mycompany.GUI.abm.AltaProveedores;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Proveedor;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeleteRelationsDialog extends JDialog {

    public enum Accion { DEJAR_SIN_PROVEEDOR, REASIGNAR }

    private static final String NUEVA_OPCION = "+ Nuevo proveedor...";

    private Accion accionElegida = null;
    private Proveedor proveedorElegido = null;
    private final int idAEliminar;

    private JRadioButton rbDejar;
    private JRadioButton rbReasignar;
    private JComboBox<Object> cbProveedores;

    private final Controladora control = new Controladora();

    public DeleteRelationsDialog(Window owner, Proveedor aEliminar, int cantProductos, List<Proveedor> otrosProveedores) {
        super(owner, "Confirmar eliminación", ModalityType.APPLICATION_MODAL);
        this.idAEliminar = aEliminar.getId();
        initUI(aEliminar, cantProductos, otrosProveedores);
        pack();
        setMinimumSize(new Dimension(420, 240));
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void initUI(Proveedor aEliminar, int cantProductos, List<Proveedor> otrosProveedores) {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(20, 24, 16, 24));
        setContentPane(root);

        String html = "<html>El proveedor <b>\"" + aEliminar.getNombre() + "\"</b> tiene <b>"
                + cantProductos + "</b> producto(s) asociado(s).<br><br>"
                + "¿Qué querés hacer con esos productos?</html>";
        root.add(new JLabel(html), BorderLayout.NORTH);

        JPanel optPanel = new JPanel();
        optPanel.setLayout(new BoxLayout(optPanel, BoxLayout.Y_AXIS));
        optPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

        rbDejar = new JRadioButton("Dejarlos sin proveedor");
        rbReasignar = new JRadioButton("Reasignarlos a otro proveedor");
        rbDejar.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbDejar);
        bg.add(rbReasignar);

        cbProveedores = new JComboBox<>();
        poblarCombo(otrosProveedores);
        cbProveedores.setEnabled(false);
        cbProveedores.setRenderer(buildRenderer());
        cbProveedores.addActionListener(e -> onComboAction());

        rbDejar.addActionListener(e -> cbProveedores.setEnabled(false));
        rbReasignar.addActionListener(e -> cbProveedores.setEnabled(true));

        optPanel.add(rbDejar);
        optPanel.add(Box.createVerticalStrut(4));
        optPanel.add(rbReasignar);
        optPanel.add(Box.createVerticalStrut(8));
        optPanel.add(cbProveedores);

        root.add(optPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnConfirmar = new JButton("Confirmar");
        btnCancelar.addActionListener(e -> dispose());
        btnConfirmar.addActionListener(e -> confirmar());
        btnPanel.add(btnCancelar);
        btnPanel.add(btnConfirmar);
        root.add(btnPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirmar);
    }

    private void poblarCombo(List<Proveedor> proveedores) {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        for (Proveedor p : proveedores) model.addElement(p);
        model.addElement(NUEVA_OPCION);
        cbProveedores.setModel(model);
    }

    private ListCellRenderer<Object> buildRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Proveedor p) {
                    setText(p.getNombre());
                } else if (NUEVA_OPCION.equals(value)) {
                    setText(NUEVA_OPCION);
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                }
                return this;
            }
        };
    }

    private void onComboAction() {
        if (!NUEVA_OPCION.equals(cbProveedores.getSelectedItem())) return;

        cbProveedores.hidePopup();

        Frame frame = findOwnerFrame();
        AltaProveedores altaDlg = new AltaProveedores(frame, true, () -> {});
        altaDlg.setLocationRelativeTo(this);
        altaDlg.setVisible(true);

        List<Proveedor> actualizados = control.traerProveedores().stream()
                .filter(p -> p.getId() != idAEliminar)
                .collect(Collectors.toList());

        poblarCombo(actualizados);

        if (!actualizados.isEmpty()) {
            cbProveedores.setSelectedItem(actualizados.get(actualizados.size() - 1));
        }
    }

    private Frame findOwnerFrame() {
        Window w = getOwner();
        while (w != null && !(w instanceof Frame)) {
            w = w.getOwner();
        }
        return (w instanceof Frame f) ? f : null;
    }

    private void confirmar() {
        if (rbDejar.isSelected()) {
            accionElegida = Accion.DEJAR_SIN_PROVEEDOR;
        } else {
            Object selected = cbProveedores.getSelectedItem();
            if (!(selected instanceof Proveedor)) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un proveedor al que reasignar los productos.",
                        "Sin selección", JOptionPane.WARNING_MESSAGE);
                return;
            }
            proveedorElegido = (Proveedor) selected;
            accionElegida = Accion.REASIGNAR;
        }
        dispose();
    }

    public Accion getAccionElegida() {
        return accionElegida;
    }

    public Proveedor getProveedorElegido() {
        return proveedorElegido;
    }
}
