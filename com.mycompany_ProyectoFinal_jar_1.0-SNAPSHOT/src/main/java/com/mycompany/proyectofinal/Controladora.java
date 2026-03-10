
package com.mycompany.proyectofinal;

import com.mycompany.persistencia.ControladoraPersistencia;
import java.util.List;
import com.mycompany.proyectofinal.Cliente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Controladora {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    private int loggedInUserId = -1;
    public String userRole;
    private Usuario loggedUser;
    
    //USUARIO
    //LOGICA DE VALIDAR USUARIO PARA LOGIN
    public boolean validarUsuario(String user, String pass){
        loggedUser = null; 
        
        List<Usuario> listaUsuarios = controlPersis.traerUsuarios();


        for (Usuario usu : listaUsuarios) {
            if (usu.getUsername().equals(user) &&
                usu.getPassword().equals(pass)) {
                Session.setCurrentUser(usu);
                return true; // Termina el Loop al encontrar un usuario en la BD que coincide con el user y pass ingresados en login form
            }
        }

        return false; // no encontro ninguno que coincida
        
    }
    
    //Metodo para saber que usuario inicio sesion actualmente
    
    public int traerIdUsuario(String user, String pass){
        for (Usuario usu : controlPersis.traerUsuarios()) {
            if (usu.getUsername().equals(user) &&
                usu.getPassword().equals(pass)) {

                return usu.getId(); 
            }
        }

        return -1; 
    }
    
    public boolean checkIfUsuReferenced(int id){
        return controlPersis.checkIfUsuReferenced(id);
    }
    
    //VALIDAR QUE USUARIO COINCIDA CON DNI PARA RECUPERAR CONTRAESÑA
    public boolean validarUsuarioYDni(String usuario, String dni) {
        return controlPersis.validarUsuarioYDni(usuario, dni);
    }
        
    
    
    
    //LOGICA DE ALTA DE USUARIO
    public void guardarUser(String user, String pass, String nombre, String apellido, String tel, String rol, String dni) {
        // Crear una nueva instancia de Usuario
        Usuario nuevoUsuario = new Usuario();
        
        // Asignar los valores a las propiedades del objeto Usuario
        nuevoUsuario.setUsername(user);
        nuevoUsuario.setPassword(pass);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setTelefono(tel);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setDni(dni);
        // Llamar al método para guardar el usuario en la base de datos
        controlPersis.guardar(nuevoUsuario);
    }
    
    //READ USUARIOS
    public List <Usuario> traerUsuarios(){
        
        
        return controlPersis.traerUsuarios();
       
    }
    //by id
    

    public Usuario findUsuario(int numEmpleado) {
       
        return controlPersis.findUsuario(numEmpleado);
    }
    
    public boolean doesUsernameExist(String username) {
            return controlPersis.doesUsernameExist(username);
    }
    
 
    public void resetPassword(String user, String newPass) {
        controlPersis.resetPassword(user, newPass);
    }

    
    //DELETE USUARIO
    public void borrarUsuario(int numUsuario) {
        controlPersis.borrarUsuario(numUsuario);
    }
    
    //edit
    public void modificarUsuario(Usuario usu, String user, String pass, String nombre, String apellido, String tel, String rol, String dni) {
        
        usu.setUsername(user);
        usu.setPassword(pass);
        usu.setNombre(nombre);
        usu.setApellido(apellido);
        usu.setRol(rol);
        usu.setTelefono(tel);
        usu.setDni(dni);
        
        controlPersis.modificarUsuario(usu);
    }
    
    
    //CLIENTE
    //CREATE
    public void guardarCliente(String nombre, String apellido, String telefono, String genero){
        Cliente nuevoCliente = new Cliente();
        
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setGenero(genero);
        
        controlPersis.guardarCliente(nuevoCliente);
        
    }
    //READ
    public List <Cliente> traerClientes(){
        
        
        return controlPersis.traerClientes();
       
    }
    
        //by id
    
    public Cliente findCliente(int id){
        return controlPersis.findCliente(id);
    }
    
    
    //UPDATE
    public void modificarCliente(Cliente cli, String nombre, String apellido, String telefono, String genero) {
        
        cli.setApellido(apellido);
        cli.setNombre(nombre);
        cli.setGenero(genero);
        cli.setTelefono(telefono);
        
        controlPersis.modificarCliente(cli);
        
    }
    
    //DELETE
    public void borrarCliente(int numCliente) {
        controlPersis.borrarCliente(numCliente);
    }
    
    public boolean checkIfClientReferenced(int id){
        return controlPersis.checkIfClientReferenced(id);
    }
    
    
    
    
    
    //PROVEEDOR
    //ALTA
    public void guardarProveedor(String nombre, String telefono, String email, String web){
        Proveedor nuevoProveedor = new Proveedor();
        
        nuevoProveedor.setNombre(nombre);
        nuevoProveedor.setTelefono(telefono);
        nuevoProveedor.setEmail(email);
        nuevoProveedor.setWebsite(web);
        
        controlPersis.guardarProveedor(nuevoProveedor);
    }
    public Proveedor findProveedor(int idProveedor) {
       
        return controlPersis.findProveedor(idProveedor);
    }
    public List<Proveedor> traerProveedores() {
        return controlPersis.traerProveedores();
    }

    public void borrarProveedor(int numProv) {
        controlPersis.borrarProveedor(numProv);
    }

    public void modificarProveedor(Proveedor prov, String nombre, String telefono, String email, String web) {
        prov.setEmail(email);
        prov.setNombre(nombre);
        prov.setTelefono(telefono);
        prov.setWebsite(web);
        
        controlPersis.modificarProveedor(prov);
    
    }
    //PRODUCTO
    //ALTA
    public void guardarProducto(String nombre, double stock, double minimo, Proveedor proveedor){
        Producto nuevoProducto = new Producto();
        
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setStock(stock);
        nuevoProducto.setMinimo(minimo);
        nuevoProducto.setProveedor(proveedor);
        
        controlPersis.guardarProducto(nuevoProducto);
        
    }
    
    
    //SERVICIO
    //ALTA
        public void guardarServicio(Servicio servicio){
        //Servicio nuevoServicio = new Servicio();
        /*
        nuevoServicio.setNombre(nombre);
        nuevoServicio.setPrecio(precio);
        nuevoServicio.setEmpleado(empleado);
        
        for (ServicioProducto sp : productos) {
            nuevoServicio.addProducto(sp);
        }
        */
        controlPersis.guardarServicio(servicio);
        
    }
      //  
         public List<Producto> traerProductos() {
        
            return controlPersis.traerProductos();

        }
         //ELIM
    public void borrarProducto(int numProducto) {
        controlPersis.borrarProducto(numProducto);
    }
    //BUSCAR X ID
    public Producto findProducto(int numProducto) {
        return controlPersis.findProducto(numProducto);
    }
    //MODIF
    public void modificarProducto(Producto prod, String nombre, double stock, double minimo, Proveedor proveedor) {
        prod.setMinimo(minimo);
        prod.setStock(stock);
        prod.setNombre(nombre);
        prod.setProveedor(proveedor);
        controlPersis.modificarProducto(prod);
                
    }
    //READ
        public List <Servicio> traerServicios(){


            return controlPersis.traerServicios();

        }
     //ELIMINAR
        public void borrarServicio(int id){
            
            controlPersis.borrarServicio(id);
        }
        
    //TURNOS
        //ALTA
        public void guardarTurno(Servicio servicio, LocalDateTime fecha, Cliente cliente, String estado, String detalle){
        Turno nuevoTurno = new Turno();
        
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setFecha(fecha);
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEstado(estado);
        nuevoTurno.setDetalle(detalle);
        
        controlPersis.guardarTurno(nuevoTurno);
        
    }
        
        //READ
        public List <Turno> traerTurnos(){


            return controlPersis.traerTurnos();

        }
        //by id
        public Turno findTurno(int id){
            return controlPersis.findTurno(id);
        }
        
        //DELETE
        
        public void borrarTurno(int id){
            
            controlPersis.borrarTurno(id);
        }
        
        public boolean turnoYaExiste(Servicio servicio, LocalDateTime fecha){
            return controlPersis.turnoYaExiste(servicio, fecha);
        }
        
        public boolean turnoYaExiste2(Servicio servicio, LocalDateTime fecha, int id){
        
            return controlPersis.turnoYaExiste2(servicio, fecha, id);
        }
        
    public void modificarTurno(Turno tur, Servicio servicio, LocalDateTime fechafinal, Cliente clienteEnt, String estado, String detalle) {
        
        
        tur.setServicio(servicio);
        tur.setCliente(clienteEnt);
        tur.setFecha(fechafinal);
        tur.setEstado(estado);
        tur.setDetalle(detalle);
        
        controlPersis.modificarTurno(tur);
        
    
    }

    public Servicio findServicio(int numServicio) {
    
            return controlPersis.findServicio(numServicio);
    }

    public void modificarServicio(Servicio ser) {
       
        
        controlPersis.modificarServicio(ser);
    
    }
    
    public boolean checkIfReferenced(int id){
        return controlPersis.checkIfReferenced(id);
    }
    
    //usuario LOGUEADO

     public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
    }

    public int getLoggedInUserId() {
        return this.loggedInUserId;
    }

    public String getUserRole(int userId) {
        Usuario user = controlPersis.findUsuario(userId);
        return user != null ? user.getRol() : null; // Returns the role or null if user not found
    }

    public void setLoggedInUserRole(String role) {
        this.userRole = role;
    }

    public String getLoggedInUserRole() {
        return userRole;
    }

   
    public Usuario getLoggedUser() {
        return loggedUser;
    }

    

    //CAJA
        //ALTA
        public void guardarConcepto(String tipo, Double monto, String medio,LocalDateTime fecha, String detalle){
        Caja nuevoConcepto = new Caja();
        
        nuevoConcepto.setTipo(tipo);
        nuevoConcepto.setDetalle(detalle);
        nuevoConcepto.setMedio(medio);
        nuevoConcepto.setMonto(monto);
        nuevoConcepto.setFecha(fecha);
        
        controlPersis.guardarCaja(nuevoConcepto);
        
    }
        
        //READ all
        public List <Caja> traerConceptos(){


            return controlPersis.traerConceptos();

        }
        //by id
        public Caja findConcepto(int id){
            return controlPersis.findConcepto(id);
        }
        
        //DELETE
        
        public void borrarConcepto(int id){
            
            controlPersis.borrarConcepto(id);
        }
        
        //EDIT
        public void modificarConcepto(Caja concepto, String tipo, double monto, String medio, String detalle) {
            concepto.setDetalle(detalle);
            concepto.setMedio(medio);
            concepto.setMonto(monto);
            concepto.setTipo(tipo);
            
            controlPersis.modificarConcepto(concepto);

        }
    
    
    
}