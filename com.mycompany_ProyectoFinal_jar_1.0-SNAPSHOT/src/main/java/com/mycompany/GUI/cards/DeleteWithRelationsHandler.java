package com.mycompany.GUI.cards;

import com.mycompany.persistencia.ProductoJpaController;
import com.mycompany.persistencia.ProveedorJpaController;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Proveedor;
import java.awt.Component;
import java.awt.Window;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DeleteWithRelationsHandler {

    public static void handleDeleteProveedor(Component parent, int proveedorId, Runnable onSuccess) {
        ProveedorJpaController provJpa = new ProveedorJpaController();
        ProductoJpaController prodJpa = new ProductoJpaController();

        Proveedor proveedor = provJpa.findProveedor(proveedorId);
        if (proveedor == null) {
            JOptionPane.showMessageDialog(parent, "Proveedor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Producto> productos = prodJpa.findByProveedor(proveedorId);

        if (productos.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(parent,
                    "¿Está seguro que desea eliminar este proveedor?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            provJpa.destroy(proveedorId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Proveedor> otrosProveedores = provJpa.findProveedorEntities().stream()
                .filter(p -> p.getId() != proveedorId)
                .collect(Collectors.toList());

        Window window = SwingUtilities.getWindowAncestor(parent);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(window, proveedor, productos.size(), otrosProveedores);
        dialog.setVisible(true);

        DeleteRelationsDialog.Accion accion = dialog.getAccionElegida();
        if (accion == null) return;

        if (accion == DeleteRelationsDialog.Accion.DEJAR_SIN_PROVEEDOR) {
            prodJpa.nullifyProveedor(proveedorId);
        } else {
            Proveedor nuevo = dialog.getProveedorElegido();
            prodJpa.reassignProveedor(proveedorId, nuevo);
        }

        provJpa.destroy(proveedorId);
        if (onSuccess != null) onSuccess.run();
    }
}
