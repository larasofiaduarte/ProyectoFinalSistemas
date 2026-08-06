package com.mycompany.GUI.abm;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.components.Btn;
import com.mycompany.controladora.Controladora;
import com.mycompany.proyectofinal.Categoria;
import javax.swing.*;
import java.awt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Alta simple de Categoria: nombre + unidad (ml/gr/unidades). Usado por cualquier combo de
 *  categoría que ofrezca la opción "+ Nueva categoría..." (Inventario, AltaProductos). */
public class AltaCategorias extends JDialog {

    private static final Logger logger = LogManager.getLogger(AltaCategorias.class);
    private static final String[] UNIDADES = {"ml", "gr", "unidades"};

    private final Controladora control = new Controladora();
    private final Runnable onSave;
    private JTextField txtNombre;
    private JComboBox<String> cmbUnidad;
    private Categoria categoriaCreada;

    public AltaCategorias(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setTitle("Nueva Categoría");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Styles.bgLight);
        getContentPane().setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Nueva Categoría");
        lblTitle.setFont(Styles.fontTitle);
        lblTitle.setForeground(Styles.fontDark);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        getContentPane().add(lblTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridBagLayout());
        panelCenter.setBackground(Styles.bgLight);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCenter.add(new JLabel("Nombre*"), gbc);

        txtNombre = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 0;
        panelCenter.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCenter.add(new JLabel("Unidad*"), gbc);

        cmbUnidad = new JComboBox<>(UNIDADES);
        gbc.gridx = 1; gbc.gridy = 1;
        panelCenter.add(cmbUnidad, gbc);

        getContentPane().add(panelCenter, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(Styles.bgLight);

        Btn btnGuardar = Btn.primary("Guardar");
        btnGuardar.setPreferredSize(Styles.btnSizeSm);
        btnGuardar.addActionListener(e -> guardarCategoria());
        panelSouth.add(btnGuardar);

        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        btnCerrar.addActionListener(e -> dispose());
        panelSouth.add(btnCerrar);

        getContentPane().add(panelSouth, BorderLayout.SOUTH);

        pack();
    }

    private void guardarCategoria() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe asignar un nombre a la categoría.",
                "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String unidad = (String) cmbUnidad.getSelectedItem();
        try {
            control.crearCategoria(nombre, unidad);
            // crearCategoria no devuelve la entidad — se busca por nombre (único) para
            // que el combo que abrió este diálogo pueda auto-seleccionar la recién creada.
            categoriaCreada = control.traerCategorias().stream()
                .filter(c -> c.getNombre().equals(nombre))
                .findFirst().orElse(null);
            JOptionPane.showMessageDialog(this, "Categoría guardada correctamente.",
                "Categoría guardada", JOptionPane.INFORMATION_MESSAGE);
            if (onSave != null) onSave.run();
            dispose();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "La categoría \"" + nombre + "\" ya existe.",
                "Categoría duplicada", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            logger.error("Error al guardar la categoría '{}'", nombre, e);
            JOptionPane.showMessageDialog(this,
                "Ocurrió un error al guardar la categoría.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Categoria persistida en esta sesión del diálogo, o null si se cerró sin guardar. */
    public Categoria getCategoriaCreada() {
        return categoriaCreada;
    }
}
