package com.mycompany.GUI.cards;

import com.mycompany.GUI.abm.AltaProveedores;
import com.mycompany.persistencia.ClienteJpaController;
import com.mycompany.persistencia.ProductoJpaController;
import com.mycompany.persistencia.ProveedorJpaController;
import com.mycompany.persistencia.ServicioJpaController;
import com.mycompany.persistencia.ServicioProductoJpaController;
import com.mycompany.persistencia.TurnoJpaController;
import com.mycompany.persistencia.UsuarioJpaController;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DeleteWithRelationsHandler {

    // -------------------------------------------------------------------------
    // 1. Proveedor → Producto
    // -------------------------------------------------------------------------
    public static void handleDeleteProveedor(Component parent, int proveedorId, Runnable onSuccess) {
        ProveedorJpaController provJpa = new ProveedorJpaController();
        ProductoJpaController prodJpa = new ProductoJpaController();

        Proveedor proveedor = provJpa.findProveedor(proveedorId);
        if (proveedor == null) { showError(parent, "Proveedor no encontrado."); return; }

        List<Producto> productos = prodJpa.findByProveedor(proveedorId);

        if (productos.isEmpty()) {
            if (!confirmar(parent, "¿Eliminar el proveedor \"" + proveedor.getNombre() + "\"?")) return;
            provJpa.destroy(proveedorId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Proveedor> otros = provJpa.findProveedorEntities().stream()
                .filter(p -> p.getId() != proveedorId).collect(Collectors.toList());

        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame frame = findFrame(window);

        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window,
                "<html>El proveedor <b>\"" + proveedor.getNombre() + "\"</b> tiene <b>"
                        + productos.size() + "</b> producto(s) asociado(s).<br><br>"
                        + "¿Qué querés hacer con esos productos?</html>",
                "Dejarlos sin proveedor",
                "Reasignarlos a otro proveedor",
                otros,
                obj -> ((Proveedor) obj).getNombre(),
                () -> {
                    AltaProveedores alta = new AltaProveedores(frame, true, () -> {});
                    alta.setLocationRelativeTo(window);
                    alta.setVisible(true);
                },
                () -> provJpa.findProveedorEntities().stream()
                        .filter(p -> p.getId() != proveedorId)
                        .collect(Collectors.toList())
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            prodJpa.nullifyProveedor(proveedorId);
        } else {
            Proveedor nuevo = (Proveedor) dialog.getSelectedItem();
            prodJpa.reassignProveedor(proveedorId, nuevo);
        }
        provJpa.destroy(proveedorId);
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // 2. Servicio → Turno  (ServicioProducto cascade-deleted automatically)
    // -------------------------------------------------------------------------
    public static void handleDeleteServicio(Component parent, int servicioId, Runnable onSuccess) {
        ServicioJpaController serJpa = new ServicioJpaController();
        TurnoJpaController turJpa = new TurnoJpaController();

        Servicio servicio = serJpa.findServicio(servicioId);
        if (servicio == null) { showError(parent, "Servicio no encontrado."); return; }

        List<Turno> activeTurnos = turJpa.findActiveByServicio(servicioId);
        int nProductos = servicio.getProductos() != null ? servicio.getProductos().size() : 0;

        String productosInfo = nProductos > 0
                ? "<br>Además, <b>" + nProductos + "</b> relación(es) con productos serán eliminadas." : "";

        if (activeTurnos.isEmpty()) {
            String msg = "<html>¿Eliminar el servicio <b>\"" + servicio.getNombre() + "\"</b>?"
                    + productosInfo + "</html>";
            if (!confirmar(parent, msg)) return;
            serJpa.destroy(servicioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Servicio> otros = serJpa.findServicioEntities().stream()
                .filter(s -> s.getId() != servicioId).collect(Collectors.toList());

        String msg = "<html>El servicio <b>\"" + servicio.getNombre() + "\"</b> tiene <b>"
                + activeTurnos.size() + "</b> turno(s) activo(s)." + productosInfo
                + "<br><br>¿Qué querés hacer con los turnos?</html>";

        Window window = SwingUtilities.getWindowAncestor(parent);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Cancelar turnos",
                "Reasignar a otro servicio",
                otros,
                obj -> ((Servicio) obj).getNombre()
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            turJpa.cancelByServicio(servicioId);
        } else {
            Servicio nuevo = (Servicio) dialog.getSelectedItem();
            turJpa.reassignServicio(servicioId, nuevo.getId());
        }
        serJpa.destroy(servicioId);
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // 3. Producto → ServicioProducto
    // -------------------------------------------------------------------------
    public static void handleDeleteProducto(Component parent, int productoId, Runnable onSuccess) {
        ProductoJpaController prodJpa = new ProductoJpaController();
        ServicioProductoJpaController spJpa = new ServicioProductoJpaController();

        Producto producto = prodJpa.findProducto(productoId);
        if (producto == null) { showError(parent, "Producto no encontrado."); return; }

        List<ServicioProducto> spList = spJpa.findByProducto(productoId);

        if (spList.isEmpty()) {
            if (!confirmar(parent, "¿Eliminar el producto \"" + producto.getNombre() + "\"?")) return;
            prodJpa.destroy(productoId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Producto> otros = prodJpa.findProductoEntities().stream()
                .filter(p -> p.getId() != productoId).collect(Collectors.toList());

        String msg = "<html>El producto <b>\"" + producto.getNombre() + "\"</b> está asociado a <b>"
                + spList.size() + "</b> servicio(s).<br><br>"
                + "¿Qué querés hacer con esas relaciones?</html>";

        Window window = SwingUtilities.getWindowAncestor(parent);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Eliminar las relaciones",
                "Reemplazar por otro producto",
                otros,
                obj -> ((Producto) obj).getNombre()
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            spJpa.deleteByProducto(productoId);
        } else {
            Producto nuevo = (Producto) dialog.getSelectedItem();
            spJpa.replaceProducto(productoId, nuevo.getId());
        }
        prodJpa.destroy(productoId);
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // 4. Cliente → Turno  (soft delete only — hard delete NOT allowed)
    // -------------------------------------------------------------------------
    public static void handleDeleteCliente(Component parent, int clienteId, Runnable onSuccess) {
        ClienteJpaController cliJpa = new ClienteJpaController();
        TurnoJpaController turJpa = new TurnoJpaController();

        Cliente cliente = cliJpa.findCliente(clienteId);
        if (cliente == null) { showError(parent, "Cliente no encontrado."); return; }

        List<Turno> activeTurnos = turJpa.findActiveByCliente(clienteId);

        if (activeTurnos.isEmpty()) {
            if (!confirmar(parent, "<html>¿Dar de baja al cliente <b>\""
                    + cliente + "\"</b>?<br>El cliente quedará inactivo.</html>")) return;
            cliJpa.softDelete(clienteId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        String msg = "<html>El cliente <b>\"" + cliente + "\"</b> tiene <b>"
                + activeTurnos.size() + "</b> turno(s) activo(s).<br><br>"
                + "¿Qué querés hacer?</html>";

        Window window = SwingUtilities.getWindowAncestor(parent);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Solo dar de baja al cliente (mantener turnos)",
                "Cancelar turnos y dar de baja al cliente"
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        if (dialog.getChoice() == DeleteRelationsDialog.Choice.B) {
            turJpa.cancelByCliente(clienteId);
        }
        cliJpa.softDelete(clienteId);
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // 5. Empleado (Usuario) → Turno (via Servicio)
    // -------------------------------------------------------------------------
    public static void handleDeleteEmpleado(Component parent, int usuarioId, Runnable onSuccess) {
        UsuarioJpaController usuJpa = new UsuarioJpaController();
        ServicioJpaController serJpa = new ServicioJpaController();
        TurnoJpaController turJpa = new TurnoJpaController();

        Usuario usuario = usuJpa.findUsuario(usuarioId);
        if (usuario == null) { showError(parent, "Empleado no encontrado."); return; }

        List<Servicio> servicios = serJpa.findByEmpleado(usuarioId);
        List<Turno> activeTurnos = turJpa.findActiveByEmpleado(usuarioId);

        if (servicios.isEmpty()) {
            if (!confirmar(parent, "¿Eliminar el empleado \"" + usuario + "\"?")) return;
            usuJpa.destroy(usuarioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Usuario> otros = usuJpa.findUsuarioEntities().stream()
                .filter(u -> u.getId() != usuarioId).collect(Collectors.toList());

        String turnosInfo = activeTurnos.isEmpty() ? ""
                : " con <b>" + activeTurnos.size() + "</b> turno(s) activo(s)";

        String msg = "<html>El empleado <b>\"" + usuario + "\"</b> tiene <b>"
                + servicios.size() + "</b> servicio(s) asignado(s)" + turnosInfo
                + ".<br><br>¿Qué querés hacer?</html>";

        Window window = SwingUtilities.getWindowAncestor(parent);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Cancelar turnos activos y desvincular servicios",
                "Reasignar servicios a otro empleado",
                otros,
                obj -> obj.toString()
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            turJpa.cancelByEmpleado(usuarioId);
            serJpa.nullifyEmpleado(usuarioId);
        } else {
            Usuario nuevo = (Usuario) dialog.getSelectedItem();
            serJpa.reassignEmpleado(usuarioId, nuevo.getId());
        }
        usuJpa.destroy(usuarioId);
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static boolean confirmar(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg,
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static Frame findFrame(Window w) {
        while (w != null && !(w instanceof Frame)) w = w.getOwner();
        return (w instanceof Frame f) ? f : null;
    }
}
