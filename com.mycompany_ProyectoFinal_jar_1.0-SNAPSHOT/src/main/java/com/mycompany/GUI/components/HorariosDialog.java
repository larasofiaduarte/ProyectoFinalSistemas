package com.mycompany.GUI.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class HorariosDialog extends JDialog {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    private JRadioButton rbCorrido;
    private JRadioButton rbCortado;
    private ButtonGroup grupoModo;

    private JPanel panelCorrido;
    private JPanel panelCortado;

    private JTextField txtInicioCorrido;
    private JTextField txtFinCorrido;

    private JTextField txtInicioManana;
    private JTextField txtFinManana;
    private JTextField txtInicioTarde;
    private JTextField txtFinTarde;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean guardado = false;
    private List<LocalTime[]> intervalosGuardados;

    public HorariosDialog(Window parent) {
        super(parent, "Horarios de apertura", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 8));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(12, 16, 12, 16));

        // --- Selector de modo ---
        JPanel panelModo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rbCorrido = new JRadioButton("Horario corrido");
        rbCortado = new JRadioButton("Horario cortado");
        grupoModo = new ButtonGroup();
        grupoModo.add(rbCorrido);
        grupoModo.add(rbCortado);
        rbCorrido.setSelected(true);
        panelModo.add(rbCorrido);
        panelModo.add(rbCortado);

        // --- Panel corrido ---
        panelCorrido = new JPanel(new GridLayout(2, 2, 8, 8));
        panelCorrido.setBorder(BorderFactory.createTitledBorder("Horario corrido"));
        panelCorrido.add(new JLabel("Inicio:"));
        txtInicioCorrido = new JTextField("08:00");
        panelCorrido.add(txtInicioCorrido);
        panelCorrido.add(new JLabel("Fin:"));
        txtFinCorrido = new JTextField("20:00");
        panelCorrido.add(txtFinCorrido);

        // --- Panel cortado ---
        panelCortado = new JPanel(new GridLayout(4, 2, 8, 8));
        panelCortado.setBorder(BorderFactory.createTitledBorder("Horario cortado"));
        panelCortado.add(new JLabel("Mañana inicio:"));
        txtInicioManana = new JTextField("08:00");
        panelCortado.add(txtInicioManana);
        panelCortado.add(new JLabel("Mañana fin:"));
        txtFinManana = new JTextField("12:00");
        panelCortado.add(txtFinManana);
        panelCortado.add(new JLabel("Tarde inicio:"));
        txtInicioTarde = new JTextField("16:00");
        panelCortado.add(txtInicioTarde);
        panelCortado.add(new JLabel("Tarde fin:"));
        txtFinTarde = new JTextField("20:00");
        panelCortado.add(txtFinTarde);

        panelCorrido.setVisible(true);
        panelCortado.setVisible(false);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.add(panelCorrido);
        panelCenter.add(panelCortado);

        // --- Toggle ---
        rbCorrido.addActionListener(e -> {
            panelCorrido.setVisible(true);
            panelCortado.setVisible(false);
            pack();
        });
        rbCortado.addActionListener(e -> {
            panelCorrido.setVisible(false);
            panelCortado.setVisible(true);
            pack();
        });

        // --- Botones ---
        btnGuardar  = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);

        add(panelModo,   BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(btnPanel,    BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnGuardar);

        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }

    private void guardar() {
        try {
            if (rbCorrido.isSelected()) {
                LocalTime inicio = LocalTime.parse(txtInicioCorrido.getText().trim(), FMT);
                LocalTime fin    = LocalTime.parse(txtFinCorrido.getText().trim(), FMT);
                if (!inicio.isBefore(fin)) {
                    error("La hora de inicio debe ser anterior a la hora de fin.");
                    return;
                }
                intervalosGuardados = List.<LocalTime[]>of(new LocalTime[]{inicio, fin});
            } else {
                LocalTime mIni = LocalTime.parse(txtInicioManana.getText().trim(), FMT);
                LocalTime mFin = LocalTime.parse(txtFinManana.getText().trim(), FMT);
                LocalTime tIni = LocalTime.parse(txtInicioTarde.getText().trim(), FMT);
                LocalTime tFin = LocalTime.parse(txtFinTarde.getText().trim(), FMT);
                if (!mIni.isBefore(mFin)) {
                    error("Mañana: el inicio debe ser anterior al fin.");
                    return;
                }
                if (!tIni.isBefore(tFin)) {
                    error("Tarde: el inicio debe ser anterior al fin.");
                    return;
                }
                if (mFin.isAfter(tIni)) {
                    error("El turno de mañana se superpone con el de tarde.");
                    return;
                }
                intervalosGuardados = List.<LocalTime[]>of(
                    new LocalTime[]{mIni, mFin},
                    new LocalTime[]{tIni, tFin}
                );
            }
            guardado = true;
            dispose();
        } catch (DateTimeParseException ex) {
            error("Formato inválido. Use HH:mm (ej: 08:00).");
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isGuardado() { return guardado; }
    public List<LocalTime[]> getIntervalos() { return intervalosGuardados; }
}
