package com.mycompany.GUI.cards;

import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Reusable dialog for deletion flows that have related children.
 *
 * Supports:
 *  - Two radio-button options (A and B)
 *  - Optional JComboBox shown when option B is selected
 *  - Optional "+ Nuevo..." entry in the combo (pass onNuevo + nuevoRefresh)
 */
public class DeleteRelationsDialog extends JDialog {

    public enum Choice { A, B }

    private static final String NUEVA_OPCION = "+ Nuevo...";

    private Choice choice = null;
    private Object selectedItem = null;

    private JRadioButton rbA;
    private JRadioButton rbB;
    private JComboBox<Object> cbItems;
    private final boolean hasCombo;
    private Function<Object, String> itemLabel;

    /** No combo — two plain radio options. */
    public DeleteRelationsDialog(Window owner, String message,
                                  String labelA, String labelB) {
        this(owner, message, labelA, labelB, null, null, null, null);
    }

    /** With combo for option B, no "+Nuevo...". */
    public DeleteRelationsDialog(Window owner, String message,
                                  String labelA, String labelB,
                                  List<?> comboItems,
                                  Function<Object, String> itemLabel) {
        this(owner, message, labelA, labelB, comboItems, itemLabel, null, null);
    }

    /**
     * Full constructor.
     *
     * @param comboItems   items for the combo (null = no combo)
     * @param itemLabel    how to display combo items (null = toString)
     * @param onNuevo      called when "+Nuevo..." is selected; null = no entry
     * @param nuevoRefresh reloads the list after onNuevo; null = no reload
     */
    public DeleteRelationsDialog(Window owner, String message,
                                  String labelA, String labelB,
                                  List<?> comboItems,
                                  Function<Object, String> itemLabel,
                                  Runnable onNuevo,
                                  Supplier<List<?>> nuevoRefresh) {
        super(owner, "Confirmar eliminación", ModalityType.APPLICATION_MODAL);
        this.hasCombo = (comboItems != null);
        this.itemLabel = itemLabel;
        initUI(message, labelA, labelB, comboItems, onNuevo, nuevoRefresh);
        pack();
        setMinimumSize(new Dimension(hasCombo ? 430 : 380, 230));
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void initUI(String message, String labelA, String labelB,
                         List<?> comboItems, Runnable onNuevo, Supplier<List<?>> nuevoRefresh) {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(20, 24, 16, 24));
        setContentPane(root);

        root.add(new JLabel(message), BorderLayout.NORTH);

        JPanel optPanel = new JPanel();
        optPanel.setLayout(new BoxLayout(optPanel, BoxLayout.Y_AXIS));
        optPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

        rbA = new JRadioButton(labelA);
        rbB = new JRadioButton(labelB);
        rbA.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbA);
        bg.add(rbB);

        optPanel.add(rbA);
        optPanel.add(Box.createVerticalStrut(4));
        optPanel.add(rbB);

        if (hasCombo) {
            cbItems = new JComboBox<>();
            poblarCombo(comboItems, onNuevo != null);
            cbItems.setEnabled(false);
            cbItems.setRenderer(buildRenderer());

            rbA.addActionListener(e -> cbItems.setEnabled(false));
            rbB.addActionListener(e -> cbItems.setEnabled(true));

            if (onNuevo != null) {
                cbItems.addActionListener(e -> {
                    if (!NUEVA_OPCION.equals(cbItems.getSelectedItem())) return;
                    cbItems.hidePopup();
                    onNuevo.run();
                    if (nuevoRefresh != null) {
                        List<?> refreshed = nuevoRefresh.get();
                        poblarCombo(refreshed, true);
                        if (!refreshed.isEmpty()) {
                            cbItems.setSelectedIndex(refreshed.size() - 1);
                        }
                    }
                });
            }

            optPanel.add(Box.createVerticalStrut(8));
            optPanel.add(cbItems);
        }

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

    private void poblarCombo(List<?> items, boolean includeNuevo) {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        for (Object item : items) model.addElement(item);
        if (includeNuevo) model.addElement(NUEVA_OPCION);
        cbItems.setModel(model);
    }

    private ListCellRenderer<Object> buildRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (NUEVA_OPCION.equals(value)) {
                    setText(NUEVA_OPCION);
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                } else if (value != null) {
                    setText(itemLabel != null ? itemLabel.apply(value) : value.toString());
                }
                return this;
            }
        };
    }

    private void confirmar() {
        if (rbA.isSelected()) {
            choice = Choice.A;
        } else {
            if (hasCombo) {
                Object sel = cbItems.getSelectedItem();
                if (sel == null || NUEVA_OPCION.equals(sel)) {
                    JOptionPane.showMessageDialog(this,
                            "Seleccione una opción válida.",
                            "Sin selección", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedItem = sel;
            }
            choice = Choice.B;
        }
        dispose();
    }

    public Choice getChoice() { return choice; }

    public Object getSelectedItem() { return selectedItem; }
}
