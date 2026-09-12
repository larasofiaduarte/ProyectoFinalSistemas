package com.mycompany.GUI.cards;

import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.GUI.abm.AltaEmpleados;
import com.mycompany.GUI.abm.AltaProveedores;
import com.mycompany.GUI.abm.AltaServicios;
import com.mycompany.persistencia.ClienteJpaController;
import com.mycompany.persistencia.ProductoJpaController;
import com.mycompany.persistencia.ProveedorJpaController;
import com.mycompany.persistencia.ServicioJpaController;
import com.mycompany.persistencia.ServicioProductoJpaController;
import com.mycompany.persistencia.UsuarioJpaController;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.util.DeleteWarningService;
import com.mycompany.proyectofinal.util.DialogUtil;
import com.mycompany.proyectofinal.util.EntityType;
import com.mycompany.proyectofinal.util.RelationType;
import com.mycompany.proyectofinal.util.Session;
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
                    // frame es la Ventana principal (findFrame sube por los owner hasta el JFrame
                    // raíz) — si el "Nuevo Proveedor" se guarda, refresca la pantalla de Proveedores
                    // aunque esté abierta en otra pestaña/card, igual que el resto de los dropdowns.
                    Runnable onNuevoSave = frame instanceof Ventana v ? v::recargarProveedores : () -> {};
                    AltaProveedores alta = new AltaProveedores(frame, true, onNuevoSave);
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
        // Los productos del servicio (ServicioProducto) NO se advierten acá: Servicio los tiene
        // con cascade=ALL+orphanRemoval, así que borrarlos es limpieza propia del servicio, no
        // pérdida de información — a diferencia de Turno, que es un registro independiente.
        boolean tieneTurnosAsociados = serJpa.checkIfReferenced(servicioId);
        List<RelationType> relaciones = tieneTurnosAsociados ? List.of(RelationType.TURNOS) : List.of();

        if (!tieneTurnosAsociados) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.SERVICIO, relaciones));
            if (!confirmar(parent, msg)) return;
            serJpa.destroy(servicioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Servicio> otros = serJpa.findServicioEntities().stream()
                .filter(s -> s.getId() != servicioId).collect(Collectors.toList());

        // "Eliminar turnos" borra las filas físicamente (Turno.servicio nunca es null) — el mensaje
        // deja eso explícito porque las dos opciones ya no son igual de reversibles: reasignar
        // conserva los turnos, eliminar los destruye para siempre.
        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.SERVICIO, relaciones, "¿Qué desea hacer con los turnos?")
                + "<br><br>Si elige <b>Eliminar turnos</b>, los turnos asociados se borrarán "
                + "de forma permanente y esta acción no se podrá deshacer. Si prefiere conservarlos, "
                + "use <b>Reasignar a otro servicio</b>.");

        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame frame = findFrame(window);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Eliminar turnos",
                "Reasignar a otro servicio",
                otros,
                obj -> ((Servicio) obj).getNombre(),
                () -> {
                    Runnable onNuevoSave = frame instanceof Ventana v ? v::recargarServicios : () -> {};
                    AltaServicios alta = new AltaServicios(frame, true, onNuevoSave);
                    alta.setLocationRelativeTo(window);
                    alta.setVisible(true);
                },
                () -> serJpa.findServicioEntities().stream()
                        .filter(s -> s.getId() != servicioId)
                        .collect(Collectors.toList())
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        // Reasignar/eliminar turnos + borrar el servicio corre en UNA transacción (ver
        // ServicioJpaController) — nunca queda un estado intermedio a mitad de camino.
        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            // Segunda confirmación obligatoria: esta rama borra turnos de forma permanente e
            // irreversible, así que no alcanza con la elección ya hecha en el diálogo anterior.
            boolean confirmaBorrado = confirmar(parent,
                    "¿Está seguro que desea eliminar permanentemente los turnos relacionados? "
                    + "Esta acción no se puede deshacer.");
            if (!confirmaBorrado) return;
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
    // 4. Cliente → Turno  (mismo patrón que Servicio → Turno: sin soft-delete, sin NULL)
    // -------------------------------------------------------------------------
    public static void handleDeleteCliente(Component parent, int clienteId, Runnable onSuccess) {
        ClienteJpaController cliJpa = new ClienteJpaController();

        Cliente cliente = cliJpa.findCliente(clienteId);
        if (cliente == null) { showError(parent, "Cliente no encontrado."); return; }

        // Igual que Servicio: cualquier turno que referencie a este cliente, sea cual sea su
        // estado, tiene que reasignarse o eliminarse antes de poder borrar el cliente.
        boolean tieneTurnosAsociados = cliJpa.checkIfClientReferenced(clienteId);
        List<RelationType> relaciones = tieneTurnosAsociados ? List.of(RelationType.TURNOS) : List.of();

        if (!tieneTurnosAsociados) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.CLIENTE, relaciones));
            if (!confirmar(parent, msg)) return;
            cliJpa.destroy(clienteId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Cliente> otros = cliJpa.findClienteEntities().stream()
                .filter(c -> c.getId() != clienteId).collect(Collectors.toList());

        // "Eliminar turnos" borra las filas físicamente — mismo mensaje/estructura que Servicio.
        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.CLIENTE, relaciones, "¿Qué desea hacer con los turnos?")
                + "<br><br>Si elige <b>Eliminar turnos</b>, los turnos asociados se borrarán "
                + "de forma permanente y esta acción no se podrá deshacer. Si prefiere conservarlos, "
                + "use <b>Reasignar a otro cliente</b>.");

        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame frame = findFrame(window);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Eliminar turnos",
                "Reasignar a otro cliente",
                otros,
                obj -> obj.toString(),
                () -> {
                    Runnable onNuevoSave = frame instanceof Ventana v ? v::recargarClientes : () -> {};
                    AltaClientes alta = new AltaClientes(frame, true, onNuevoSave);
                    alta.setLocationRelativeTo(window);
                    alta.setVisible(true);
                },
                () -> cliJpa.findClienteEntities().stream()
                        .filter(c -> c.getId() != clienteId)
                        .collect(Collectors.toList())
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        // Reasignar/eliminar turnos + borrar el cliente corre en UNA transacción (ver
        // ClienteJpaController) — nunca queda un estado intermedio a mitad de camino.
        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            // Segunda confirmación obligatoria, igual que Servicio: esta rama borra turnos de
            // forma permanente e irreversible.
            boolean confirmaBorrado = confirmar(parent,
                    "¿Está seguro que desea eliminar permanentemente los turnos relacionados? "
                    + "Esta acción no se puede deshacer.");
            if (!confirmaBorrado) return;
            cliJpa.deleteAndRemoveTurnos(clienteId);
        } else {
            Cliente nuevo = (Cliente) dialog.getSelectedItem();
            cliJpa.deleteAndReassignTurnos(clienteId, nuevo.getId());
        }
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // 5. Empleado (Usuario) → Turno  (mismo patrón que Cliente/Servicio → Turno: sin
    //    soft-delete, sin NULL en Turno.empleado — se reasigna o se elimina. Los Servicio que
    //    el empleado tenía asignados se desvinculan/reasignan junto con los turnos, en la misma
    //    transacción, para no romper la FK de Servicio.empleado al borrar el Usuario.)
    // -------------------------------------------------------------------------
    public static void handleDeleteEmpleado(Component parent, int usuarioId, Runnable onSuccess) {
        UsuarioJpaController usuJpa = new UsuarioJpaController();

        Usuario usuario = usuJpa.findUsuario(usuarioId);
        if (usuario == null) { showError(parent, "Empleado no encontrado."); return; }

        // Punto único de entrada real del borrado de empleados (la UI y cualquier llamador futuro
        // pasan por acá) — se valida acá, no solo en la UI, para que la regla no dependa de que el
        // caller se acuerde de chequearla antes de invocar este método.
        if (Session.isSelf(usuarioId)) {
            showError(parent, "No se puede eliminar el usuario de la sesión actual.");
            return;
        }

        // Un Dueño puede borrar empleados y otros Dueños, pero no a un Administrador —
        // protege la jerarquía de roles aunque el Dueño tenga permiso general de borrado.
        if (Session.isOwner() && Session.esAdministrador(usuario)) {
            showError(parent, "No se puede eliminar un usuario administrador.");
            return;
        }

        // Igual que Cliente/Servicio: cualquier turno que referencie a este empleado, sea cual
        // sea su estado, tiene que reasignarse o eliminarse antes de poder borrar el empleado.
        boolean tieneTurnosAsociados = usuJpa.checkIfReferencedByTurnos(usuarioId);
        List<RelationType> relaciones = tieneTurnosAsociados ? List.of(RelationType.TURNOS) : List.of();

        if (!tieneTurnosAsociados) {
            String msg = htmlWrap(DeleteWarningService.buildMessage(EntityType.EMPLEADO, relaciones));
            if (!confirmar(parent, msg)) return;
            usuJpa.destroyAndUnlinkServicios(usuarioId);
            if (onSuccess != null) onSuccess.run();
            return;
        }

        List<Usuario> otros = usuJpa.findUsuarioEntities().stream()
                .filter(u -> u.getId() != usuarioId).collect(Collectors.toList());

        // "Eliminar turnos" borra las filas físicamente — mismo mensaje/estructura que Cliente/Servicio.
        String msg = htmlWrap(DeleteWarningService.buildMessage(
                EntityType.EMPLEADO, relaciones, "¿Qué desea hacer con los turnos?")
                + "<br><br>Si elige <b>Eliminar turnos</b>, los turnos asociados se borrarán "
                + "de forma permanente y esta acción no se podrá deshacer. Si prefiere conservarlos, "
                + "use <b>Reasignar a otro empleado</b>.");

        Window window = SwingUtilities.getWindowAncestor(parent);
        Frame frame = findFrame(window);
        DeleteRelationsDialog dialog = new DeleteRelationsDialog(
                window, msg,
                "Eliminar turnos",
                "Reasignar a otro empleado",
                otros,
                obj -> obj.toString(),
                () -> {
                    Runnable onNuevoSave = frame instanceof Ventana v ? v::recargarUsuarios : () -> {};
                    AltaEmpleados alta = new AltaEmpleados(frame, true, onNuevoSave);
                    alta.setLocationRelativeTo(window);
                    alta.setVisible(true);
                },
                () -> usuJpa.findUsuarioEntities().stream()
                        .filter(u -> u.getId() != usuarioId)
                        .collect(Collectors.toList())
        );
        dialog.setVisible(true);

        if (dialog.getChoice() == null) return;

        // Reasignar/eliminar turnos + servicios + borrar el empleado corre en UNA transacción
        // (ver UsuarioJpaController) — nunca queda un estado intermedio a mitad de camino.
        if (dialog.getChoice() == DeleteRelationsDialog.Choice.A) {
            // Segunda confirmación obligatoria, igual que Cliente/Servicio: esta rama borra
            // turnos de forma permanente e irreversible.
            boolean confirmaBorrado = confirmar(parent,
                    "¿Está seguro que desea eliminar permanentemente los turnos relacionados? "
                    + "Esta acción no se puede deshacer.");
            if (!confirmaBorrado) return;
            usuJpa.deleteAndRemoveTurnos(usuarioId);
        } else {
            Usuario nuevo = (Usuario) dialog.getSelectedItem();
            usuJpa.deleteAndReassignTurnos(usuarioId, nuevo.getId());
        }
        if (onSuccess != null) onSuccess.run();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private static boolean confirmar(Component parent, String msg) {
        return DialogUtil.confirmar(parent, msg, "Confirmar eliminación");
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
