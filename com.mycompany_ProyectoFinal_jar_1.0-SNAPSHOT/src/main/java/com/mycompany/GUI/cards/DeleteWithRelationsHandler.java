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
import com.mycompany.proyectofinal.util.DeleteWarningService;
import com.mycompany.proyectofinal.util.EntityType;
import com.mycompany.proyectofinal.util.RelationType;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
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
        List<RelationType> relaciones = productos.isEmpty() ? List.of() : List.of(RelationType.PRODUCTOS);

        if (productos.isEmpty()) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.PROVEEDOR, relaciones));
            if (!confirmar(parent, msg)) return;
            provJpa.destroy(proveedorId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Proveedor> otros = provJpa.findProveedorEntities().stream()
                .filter(p -> p.getId() != proveedorId).collect(Collectors.toList());

        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame frame = findFrame(window);

        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.PROVEEDOR, relaciones, "¿Qué desea hacer con esos productos?"));

        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window,
                msg,
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

        Servicio servicio = serJpa.findServicio(servicioId);
        if (servicio == null) { showError(parent, "Servicio no encontrado."); return; }

        // Turno.servicio es NOT NULL (entidad y base) — todo turno que referencia este servicio,
        // sea cual sea su estado, tiene que reasignarse o eliminarse antes de poder borrar el
        // servicio. Por eso ya no se distingue "activos" de "todos": cualquiera dispara el popup.
        boolean tieneTurnosAsociados = serJpa.checkIfReferenced(servicioId);
        int nProductos = servicio.getProductos() != null ? servicio.getProductos().size() : 0;

        List<RelationType> relaciones = new ArrayList<>();
        if (tieneTurnosAsociados) relaciones.add(RelationType.TURNOS);
        if (nProductos > 0) relaciones.add(RelationType.PRODUCTOS);

        if (!tieneTurnosAsociados) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.SERVICIO, relaciones));
            if (!confirmar(parent, msg)) return;
            serJpa.destroy(servicioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Servicio> otros = serJpa.findServicioEntities().stream()
                .filter(s -> s.getId() != servicioId).collect(Collectors.toList());

        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.SERVICIO, relaciones, "¿Qué desea hacer con los turnos?"));

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

        // Reasignar/eliminar turnos + borrar el servicio corre en UNA transacción (ver
        // ServicioJpaController) — nunca queda un estado intermedio a mitad de camino.
        // "Cancelar turnos" acá significa eliminarlos físicamente (Turno.servicio nunca es null).
        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            serJpa.deleteAndRemoveTurnos(servicioId);
        } else {
            Servicio nuevo = (Servicio) dialog.getSelectedItem();
            serJpa.deleteAndReassignTurnos(servicioId, nuevo.getId());
        }
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
        List<RelationType> relaciones = spList.isEmpty() ? List.of() : List.of(RelationType.SERVICIOS);

        if (spList.isEmpty()) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.PRODUCTO, relaciones));
            if (!confirmar(parent, msg)) return;
            prodJpa.destroy(productoId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Producto> otros = prodJpa.findProductoEntities().stream()
                .filter(p -> p.getId() != productoId).collect(Collectors.toList());

        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.PRODUCTO, relaciones, "¿Qué desea hacer con esas relaciones?"));

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
        List<RelationType> relaciones = activeTurnos.isEmpty() ? List.of() : List.of(RelationType.TURNOS);

        // Cliente usa soft-delete (nunca se borra realmente), por eso el verbo es "dar de baja"
        // en vez del "eliminar" genérico que usa el resto de las entidades.
        if (activeTurnos.isEmpty()) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(
                    EntityType.CLIENTE, relaciones, "dar de baja", "se da de baja", "¿Desea continuar?"));
            if (!confirmar(parent, msg)) return;
            cliJpa.softDelete(clienteId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.CLIENTE, relaciones, "dar de baja", "se da de baja", "¿Qué desea hacer?"));

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

        List<RelationType> relaciones = new ArrayList<>();
        if (!servicios.isEmpty()) relaciones.add(RelationType.SERVICIOS);
        if (!activeTurnos.isEmpty()) relaciones.add(RelationType.TURNOS);

        if (servicios.isEmpty()) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.EMPLEADO, relaciones));
            if (!confirmar(parent, msg)) return;
            usuJpa.destroy(usuarioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Usuario> otros = usuJpa.findUsuarioEntities().stream()
                .filter(u -> u.getId() != usuarioId).collect(Collectors.toList());

        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.EMPLEADO, relaciones, "¿Qué desea hacer?"));

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

    // JLabel/JOptionPane solo hacen wrap de texto largo si el string arranca con <html> —
    // los mensajes de DeleteWarningService son texto plano, así que se envuelven acá.
    private static String htmlWrap(String msg) {
        return "<html>" + msg + "</html>";
    }

    private static Frame findFrame(Window w) {
        while (w != null && !(w instanceof Frame)) w = w.getOwner();
        return (w instanceof Frame f) ? f : null;
    }
}
