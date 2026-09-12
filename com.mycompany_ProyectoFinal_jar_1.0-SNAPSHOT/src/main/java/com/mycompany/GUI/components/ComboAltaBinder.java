package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * Agrega la opción "+ Nuevo..." a un JComboBox&lt;String&gt; de un formulario Alta (no confundir
 * con FilteredComboBoxEditor, que es para columnas editables de un JTable). Reproduce el patrón
 * ya usado a mano en AltaProductos.cboProv, centralizado para no reescribirlo por combo/formulario.
 */
public class ComboAltaBinder<T> {

    private final JComboBox<String> combo;
    private final JDialog host;
    private final Supplier<List<T>> fetcher;
    private final Function<T, String> nameOf;
    private final String nuevaOpcion;
    private final BiFunction<Frame, Runnable, ? extends JDialog> dialogFactory;
    private final Runnable onExternalRefresh;
    private final Runnable onSeleccionCambiada;
    private final List<String> nombres = new ArrayList<>();

    public ComboAltaBinder(
            JComboBox<String> combo,
            JDialog host,
            Supplier<List<T>> fetcher,
            Function<T, String> nameOf,
            String nuevaOpcion,
            BiFunction<Frame, Runnable, ? extends JDialog> dialogFactory,
            Runnable onExternalRefresh
    ) {
        this(combo, host, fetcher, nameOf, nuevaOpcion, dialogFactory, onExternalRefresh, null);
    }

    /**
     * @param onSeleccionCambiada callback opcional que reproduce la misma lógica que corre ante una
     * selección manual del combo (ej. recalcular datos dependientes). Se invoca explícitamente tras
     * seleccionar el ítem recién creado en {@link #recargar()} porque, al ocurrir en medio del
     * fireActionEvent original (el disparado por elegir "+ Nuevo..."), JComboBox ignora ese
     * fireActionEvent anidado (guard interno firingActionEvent) y los ActionListener normales del
     * combo no llegan a ejecutarse para esa selección.
     */
    public ComboAltaBinder(
            JComboBox<String> combo,
            JDialog host,
            Supplier<List<T>> fetcher,
            Function<T, String> nameOf,
            String nuevaOpcion,
            BiFunction<Frame, Runnable, ? extends JDialog> dialogFactory,
            Runnable onExternalRefresh,
            Runnable onSeleccionCambiada
    ) {
        this.combo = combo;
        this.host = host;
        this.fetcher = fetcher;
        this.nameOf = nameOf;
        this.nuevaOpcion = nuevaOpcion;
        this.dialogFactory = dialogFactory;
        this.onExternalRefresh = onExternalRefresh;
        this.onSeleccionCambiada = onSeleccionCambiada;
    }

    /** Construye modelo+renderer+autocomplete+listeners. Llamar una vez, en lugar del viejo obtenerX(). */
    public void cargar() {
        poblar(fetcher.get());

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (nuevaOpcion.equals(value)) {
                    setFont(getFont().deriveFont(Font.ITALIC));
                    if (!isSelected) setForeground(new Color(70, 130, 200));
                }
                return this;
            }
        });

        Styles.addAutoComplete(combo, nombres);

        // El filtro de Styles.addAutoComplete puede dejar afuera la opción "+ Nuevo..." si no
        // matchea lo tipeado — este segundo listener (se registra después, así que corre después)
        // la vuelve a agregar al final para que siempre quede alcanzable.
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE ||
                    key == KeyEvent.VK_UP    || key == KeyEvent.VK_DOWN) return;
                DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>) combo.getModel();
                if (m.getIndexOf(nuevaOpcion) < 0) m.addElement(nuevaOpcion);
            }
        });

        combo.addActionListener(e -> {
            if (!nuevaOpcion.equals(combo.getSelectedItem())) return;
            combo.hidePopup();
            Frame parent = (Frame) host.getOwner();
            Runnable onNuevoSave = onExternalRefresh != null ? onExternalRefresh : () -> {};
            JDialog dialog = dialogFactory.apply(parent, onNuevoSave);
            dialog.setLocationRelativeTo(host);
            dialog.setVisible(true);
            recargar();
        });
    }

    private void recargar() {
        List<T> actualizados = fetcher.get();
        poblar(actualizados);
        if (!actualizados.isEmpty()) {
            combo.setSelectedItem(nameOf.apply(actualizados.get(actualizados.size() - 1)));
            if (onSeleccionCambiada != null) onSeleccionCambiada.run();
        }
    }

    private void poblar(List<T> items) {
        nombres.clear();
        for (T item : items) nombres.add(nameOf.apply(item));
        nombres.add(nuevaOpcion);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String n : nombres) model.addElement(n);
        combo.setModel(model);
    }
}
