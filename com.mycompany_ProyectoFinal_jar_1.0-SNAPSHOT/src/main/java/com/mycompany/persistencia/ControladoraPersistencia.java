
package com.mycompany.persistencia;

import com.mycompany.GUI.exceptions.NonexistentEntityException;
import com.mycompany.proyectofinal.Caja;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
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

    
    
    
    //CAJA
    
    CajaJpaController cajaJpa = new CajaJpaController();
    
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
         * Descuenta el stock de cada producto usado en el servicio del turno.
         * Idempotente: si stockDescontado == true no hace nada.
         *
         * Siempre re-fetches entities fresh from DB to avoid detached/cached
         * state issues with the in-memory table model entities.
         */
        public void descontarStockProductos(Turno turno) {
            try {
                // ── 1. Re-fetch turno from DB (avoid detached-entity problems) ──
                Turno managed = turJpa.findTurno(turno.getId());
                if (managed == null) {
                    System.err.println("[StockDescuento] WARN: Turno id=" + turno.getId() + " no encontrado en DB");
                    return;
                }

                // ── 2. Idempotency guard (read from DB, not from in-memory field) ──
                if (managed.isStockDescontado()) {
                    System.out.println("[StockDescuento] Turno id=" + turno.getId() + " ya procesado — omitiendo");
                    return;
                }

                // ── 3. Verify servicio ──
                Servicio servicio = managed.getServicio();
                if (servicio == null) {
                    System.err.println("[StockDescuento] WARN: Turno id=" + turno.getId() + " sin servicio");
                    managed.setStockDescontado(true);
                    turJpa.edit(managed);
                    return;
                }

                // ── 4. Re-fetch servicio directly to guarantee productos list is loaded ──
                Servicio freshServicio = serJpa.findServicio(servicio.getId());
                List<ServicioProducto> productos =
                    (freshServicio != null) ? freshServicio.getProductos() : null;

                if (productos == null || productos.isEmpty()) {
                    System.out.println("[StockDescuento] Servicio id=" + servicio.getId()
                        + " sin productos configurados — nada que descontar");
                    managed.setStockDescontado(true);
                    turJpa.edit(managed);
                    return;
                }

                System.out.println("[StockDescuento] Turno id=" + turno.getId()
                    + " | Servicio: " + freshServicio.getNombre()
                    + " | Productos: " + productos.size());

                // ── 5. Deduct stock per product ──
                for (ServicioProducto sp : productos) {
                    Producto producto = sp.getProducto();
                    if (producto == null) {
                        System.err.println("[StockDescuento] WARN: ServicioProducto id=" + sp.getId() + " sin producto");
                        continue;
                    }

                    // Re-fetch producto fresh to read current DB stock (not stale)
                    Producto freshProducto = prodJpa.findProducto(producto.getId());
                    if (freshProducto == null) {
                        System.err.println("[StockDescuento] WARN: Producto id=" + producto.getId() + " no encontrado");
                        continue;
                    }

                    double antes   = freshProducto.getStock();
                    double despues = antes - sp.getCantidadUsada();
                    freshProducto.setStock(despues);
                    prodJpa.edit(freshProducto);

                    System.out.printf("[StockDescuento]   %-20s: %.4f - %.4f = %.4f%n",
                        freshProducto.getNombre(), antes, sp.getCantidadUsada(), despues);
                }

                // ── 6. Mark as done and persist ──
                managed.setStockDescontado(true);
                turJpa.edit(managed);
                System.out.println("[StockDescuento] Completado — Turno id=" + turno.getId()
                    + " stockDescontado=true persistido");

            } catch (Exception e) {
                System.err.println("[StockDescuento] ERROR en turno id=" + turno.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }


    

    
        
        
}
