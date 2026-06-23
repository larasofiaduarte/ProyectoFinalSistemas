
package com.mycompany.persistencia;

import com.mycompany.GUI.exceptions.NonexistentEntityException;
import com.mycompany.proyectofinal.Caja;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.MovimientoStock;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladoraPersistencia {
    //USUARIO
    UsuarioJpaController usuJpa = new UsuarioJpaController();

        public List<Usuario> traerUsuarios() {
            return usuJpa.findUsuarioEntities();
        }

        public void guardar(Usuario nuevoUsuario) {
            usuJpa.create(nuevoUsuario);
        }
        public void borrarUsuario(int numUsuario) {
            usuJpa.destroy(numUsuario); 
        }
        ///???
        public Usuario findUsuario(int numEmpleado) {
            return usuJpa.findUsuario(numEmpleado);
        }
        public boolean doesUsernameExist(String username) {
            return usuJpa.doesUsernameExist(username);
        }
        public boolean checkIfUsuReferenced(int id){
            return usuJpa.checkIfReferenced(id);
        }
        public Usuario findUsuarioByUser(String user){
            return usuJpa.findUsuarioByUsername(user);
        }
        public boolean validarUsuarioYEmail(String user, String email) {
            return usuJpa.validarUsuarioYEmail(user, email);
        }
        public void resetPassword(String user, String newPass) {
            Usuario u = usuJpa.findUsuarioByUsername(user);
            if (u != null) {
                u.setPassword(newPass); // or setPassword()
                usuJpa.edit(u);     // MERGE
            }
        }
        public void modificarUsuario(Usuario usu) {
                usuJpa.edit(usu);
        }
        
    //CLIENTE
    ClienteJpaController cliJpa = new ClienteJpaController();

        //CREATE
        public void guardarCliente(Cliente nuevoCliente){
            cliJpa.create(nuevoCliente);
        }

        //READ
        public List<Cliente> traerClientes() {
           return cliJpa.findClienteEntities();
        }
            //buscar x id
        public Cliente findCliente(int id){
            return cliJpa.findCliente(id);
        }

        //DELETE
        public void borrarCliente(int numCliente) {

                cliJpa.destroy(numCliente);
        }
        
        //UPDATE
        public void modificarCliente(Cliente cli) {
          
            cliJpa.edit(cli);
            
        }
        
        public boolean checkIfClientReferenced(int clienteId){
            return cliJpa.checkIfClientReferenced(clienteId);
        }
        
    //PROVEEDOR
        
    ProveedorJpaController provJpa = new ProveedorJpaController();

        public void guardarProveedor(Proveedor nuevoProveedor){
            provJpa.create(nuevoProveedor);
        }
        
        public Proveedor findProveedor(int id){
            return provJpa.findProveedor(id);
        }
        
        public List<Proveedor> traerProveedores() {
            return provJpa.findProveedorEntities();
        }
        public void borrarProveedor(int numProv) {
            provJpa.destroy(numProv);
        }

        public void modificarProveedor(Proveedor prov) {
            provJpa.edit(prov);
        }
        
    //PRODUCTO
    
    ProductoJpaController prodJpa = new ProductoJpaController();

        //CREATE
        public void guardarProducto(Producto nuevoProducto){
            prodJpa.create(nuevoProducto);
        }

        //READ
        public List<Producto> traerProducto() {
           return prodJpa.findProductoEntities();
        }
        
        public Producto findProducto(int id){
            return prodJpa.findProducto(id);
        }

        //DELETE
        public void borrarProducto(int numProductos) {

                prodJpa.destroy(numProductos);
        }
        
        public List<Producto> traerProductos() {
            return prodJpa.findProductoEntities();
        }
        
        public void modificarProducto(Producto prod) {
               prodJpa.edit(prod);
        }
        
   //SERVICIO
        
   ServicioJpaController serJpa = new ServicioJpaController();
        //CREATE
        public void guardarServicio(Servicio nuevoServicio){
            serJpa.create(nuevoServicio);
        }

        //READ
        public List<Servicio> traerServicios() {
           return serJpa.findServicioEntities();
        }

        //DELETE
        public void borrarServicio(int numServicios) {

                serJpa.destroy(numServicios);
        }
        
        public boolean checkIfReferenced(int servicioId){
            return serJpa.checkIfReferenced(servicioId);
        }
        
        public Servicio findServicio(int numServicio) {
            return serJpa.findServicio(numServicio);
        }

        public void modificarServicio(Servicio ser) {
            serJpa.edit(ser);
        }

        
    //TURNO
        
    TurnoJpaController turJpa = new TurnoJpaController();
    
        public void guardarTurno(Turno nuevoTurno){
            turJpa.create(nuevoTurno);
        }

        //READ
        public List<Turno> traerTurnos() {
                return turJpa.findTurnoEntities();
        }
        //by id
        public Turno findTurno(int id) {
            
            return turJpa.findTurno(id);
                
        }
        
        //DELETE
        public void borrarTurno(int numTurnos) {

            turJpa.destroy(numTurnos);
        }
        
        //UPDATE/EDIT
        public void modificarTurno(Turno tur) {

            turJpa.edit(tur);
        }
        // Método para verificar si el turno ya existe
        public boolean turnoYaExiste(Servicio servicio, LocalDateTime fechahora) {
            return turJpa.turnoYaExiste(servicio, fechahora);
        }
        
        public boolean turnoYaExiste2(Servicio servicio, LocalDateTime fechahora, int id){

            return turJpa.turnoYaExiste2(servicio,fechahora,id);
        }

        public List<Turno> traerTurnosPorEmpleadoYFecha(int empleadoId, LocalDate fecha, int excludeId) {
            return turJpa.findByEmpleadoAndFecha(empleadoId, fecha, excludeId);
        }

    
    
    
    //CAJA
    
    CajaJpaController cajaJpa = new CajaJpaController();
    MovimientoStockJpaController movStockJpa = new MovimientoStockJpaController();
    
        public void guardarCaja(Caja nuevoConcepto){
            cajaJpa.create(nuevoConcepto);
        }

        //READ
        public List<Caja> traerConceptos() {
                return cajaJpa.findCajaEntities();
        }
        //by id
        public Caja findConcepto(int id) {
            
            return cajaJpa.findCaja(id);
                
        }
        
        //DELETE
        public void borrarConcepto(int numConcepto) {

            cajaJpa.destroy(numConcepto);
        }
        
        //UPDATE/EDIT
        public void modificarConcepto(Caja concepto) {

            cajaJpa.edit(concepto);
        }

   

    
    public void finalizarTurno(Turno turno) {
            // 1. Actualizar estado del turno
            turno.setEstado("Finalizado");
            turJpa.edit(turno);

            // 2. Crear movimiento en caja automáticamente
            Caja movimiento = new Caja();
            movimiento.setTipo("Ingreso");
            movimiento.setMonto(turno.getServicio().getPrecio());
            movimiento.setDetalle("Servicio: " + turno.getServicio().getNombre() 
                + " - Cliente: " + turno.getCliente().getNombre() 
                + " " + turno.getCliente().getApellido());
            movimiento.setMedio("Efectivo"); // o podés dejarlo null / pedirlo al usuario
            movimiento.setFecha(LocalDateTime.now());

            cajaJpa.create(movimiento);
        }
        
        public void registrarIngresoEnCaja(Turno turno) {
            Caja movimiento = new Caja();
            movimiento.setTipo("Ingreso");
            movimiento.setMonto(turno.getServicio().getPrecio());
            movimiento.setDetalle("Servicio: " + turno.getServicio().getNombre()
                + " - Cliente: " + turno.getCliente().getNombre()
                + " " + turno.getCliente().getApellido());
            movimiento.setMedio("MercadoPago");
            movimiento.setFecha(LocalDateTime.now());
            movimiento.setIdTurno(turno.getId());
            cajaJpa.create(movimiento);
        }

        public boolean existsCajaByTurnoId(int turnoId) {
            return cajaJpa.existsByTurnoId(turnoId);
        }

        public void deleteCajaByTurnoId(int turnoId) {
            cajaJpa.deleteByTurnoId(turnoId);
        }

        /**
         * Discounts stock for every product in the Turno's Servicio and records
         * a SALIDA MovimientoStock per product.
         * Idempotent: does nothing if stockDescontado is already true in DB.
         */
        public void descontarStockProductos(Turno turno) {
            try {
                Turno managed = turJpa.findTurno(turno.getId());
                if (managed == null) {
                    System.err.println("[Stock] WARN: Turno id=" + turno.getId() + " no encontrado");
                    return;
                }
                // Evita descontar dos veces el mismo turno
                if (managed.isStockDescontado()) {
                    System.out.println("[Stock] Turno id=" + turno.getId() + " ya descontado — omitiendo");
                    return;
                }

                Servicio servicio = managed.getServicio();
                if (servicio == null) {
                    managed.setStockDescontado(true);
                    turJpa.edit(managed);
                    return;
                }

                // Recarga el servicio desde BD para obtener la lista actualizada de productos
                Servicio fresh = serJpa.findServicio(servicio.getId());
                List<ServicioProducto> productos = fresh != null ? fresh.getProductos() : null;

                if (productos == null || productos.isEmpty()) {
                    System.out.println("[Stock] Servicio id=" + servicio.getId() + " sin productos");
                    managed.setStockDescontado(true);
                    turJpa.edit(managed);
                    return;
                }

                System.out.println("[Stock] SALIDA — Turno id=" + turno.getId()
                    + " | " + fresh.getNombre() + " | " + productos.size() + " producto(s)");

                // Recorre cada ítem del servicio (puede ser producto específico o categoría)
                for (ServicioProducto sp : productos) {
                    if (sp.getProducto() != null) {
                        // Descuento directo: producto específico
                        Producto fp = prodJpa.findProducto(sp.getProducto().getId());
                        if (fp == null) continue;

                        double antes = fp.getStock();
                        fp.setStock(antes - sp.getCantidadUsada());
                        prodJpa.edit(fp);

                        MovimientoStock mov = new MovimientoStock();
                        mov.setProducto(fp);
                        mov.setCantidad(sp.getCantidadUsada());
                        mov.setTipo("SALIDA");
                        mov.setFecha(LocalDateTime.now());
                        mov.setTurnoId(turno.getId());
                        movStockJpa.create(mov);

                        System.out.printf("[Stock]   SALIDA %-20s: %.4f - %.4f = %.4f%n",
                            fp.getNombre(), antes, sp.getCantidadUsada(), fp.getStock());

                    } else if (sp.getCategoria() != null && !sp.getCategoria().isBlank()) {
                        // Descuento por categoría: descuenta de a uno empezando por el de mayor stock
                        List<com.mycompany.proyectofinal.Producto> enCategoria =
                            prodJpa.findByCategoria(sp.getCategoria());
                        double remaining = sp.getCantidadUsada();
                        for (com.mycompany.proyectofinal.Producto cp : enCategoria) {
                            if (remaining <= 0) break;
                            Producto fp = prodJpa.findProducto(cp.getId());
                            if (fp == null || fp.getStock() <= 0) continue;
                            // Descuenta lo que haya disponible sin pasar a negativo
                            double deduct = Math.min(fp.getStock(), remaining);
                            double antes = fp.getStock();
                            fp.setStock(antes - deduct);
                            prodJpa.edit(fp);
                            remaining -= deduct; // Actualiza cuánto falta aún por descontar

                            MovimientoStock mov = new MovimientoStock();
                            mov.setProducto(fp);
                            mov.setCantidad(deduct);
                            mov.setTipo("SALIDA");
                            mov.setFecha(LocalDateTime.now());
                            mov.setTurnoId(turno.getId());
                            movStockJpa.create(mov);

                            System.out.printf("[Stock]   SALIDA-CAT %-20s (cat=%s): %.4f - %.4f = %.4f%n",
                                fp.getNombre(), sp.getCategoria(), antes, deduct, fp.getStock());
                        }
                        if (remaining > 0) {
                            System.err.printf("[Stock] WARN: Stock insuficiente en categoría '%s', faltaron %.4f ML%n",
                                sp.getCategoria(), remaining);
                        }
                    }
                }

                managed.setStockDescontado(true);
                turJpa.edit(managed);
                System.out.println("[Stock] Completado SALIDA — Turno id=" + turno.getId());

            } catch (Exception e) {
                System.err.println("[Stock] ERROR descontar turno id=" + turno.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * Restores stock for every product in the Turno's Servicio and records
         * an ENTRADA MovimientoStock per product.
         * Previous SALIDA records are NOT deleted — this is an additive audit trail.
         * Idempotent: does nothing if stockDescontado is already false in DB.
         */
        public void revertirStockProductos(Turno turno) {
            try {
                Turno managed = turJpa.findTurno(turno.getId());
                if (managed == null) {
                    System.err.println("[Stock] WARN: Turno id=" + turno.getId() + " no encontrado");
                    return;
                }
                // Evita restaurar si el stock nunca fue descontado
                if (!managed.isStockDescontado()) {
                    System.out.println("[Stock] Turno id=" + turno.getId() + " sin stock descontado — omitiendo");
                    return;
                }

                // Busca los movimientos SALIDA del turno para revertirlos uno a uno.
                // Funciona tanto para productos específicos como para consumo por categoría.
                List<MovimientoStock> salidas = movStockJpa.findSalidasByTurnoId(turno.getId());

                if (salidas.isEmpty()) {
                    managed.setStockDescontado(false);
                    turJpa.edit(managed);
                    return;
                }

                System.out.println("[Stock] ENTRADA — Turno id=" + turno.getId()
                    + " | " + salidas.size() + " movimiento(s)");

                for (MovimientoStock salida : salidas) {
                    Producto fp = prodJpa.findProducto(salida.getProducto().getId());
                    if (fp == null) continue;

                    double antes = fp.getStock();
                    // Devuelve exactamente la cantidad que se había descontado
                    fp.setStock(antes + salida.getCantidad());
                    prodJpa.edit(fp);

                    // Registra la devolución como ENTRADA en el historial (no borra la SALIDA original)
                    MovimientoStock entrada = new MovimientoStock();
                    entrada.setProducto(fp);
                    entrada.setCantidad(salida.getCantidad());
                    entrada.setTipo("ENTRADA");
                    entrada.setFecha(LocalDateTime.now());
                    entrada.setTurnoId(turno.getId());
                    movStockJpa.create(entrada);

                    System.out.printf("[Stock]   ENTRADA %-20s: %.4f + %.4f = %.4f%n",
                        fp.getNombre(), antes, salida.getCantidad(), fp.getStock());
                }

                managed.setStockDescontado(false);
                turJpa.edit(managed);
                System.out.println("[Stock] Completado ENTRADA — Turno id=" + turno.getId());

            } catch (Exception e) {
                System.err.println("[Stock] ERROR revertir turno id=" + turno.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }


    

    
        
        
}
