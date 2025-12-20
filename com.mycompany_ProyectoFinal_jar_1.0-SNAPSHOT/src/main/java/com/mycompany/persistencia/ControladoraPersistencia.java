
package com.mycompany.persistencia;

import com.mycompany.GUI.exceptions.NonexistentEntityException;
import com.mycompany.proyectofinal.Caja;
import com.mycompany.proyectofinal.Usuario;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Servicio;
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
        
        public Usuario findUsuario(int numEmpleado) {
            return usuJpa.findUsuario(numEmpleado);
        }
        public boolean doesUsernameExist(String username) {
            return usuJpa.doesUsernameExist(username);
        }
        public boolean checkIfUsuReferenced(int id){
            return usuJpa.checkIfReferenced(id);
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

    public void modificarUsuario(Usuario usu) {
            usuJpa.edit(usu);
    }

    public Servicio findServicio(int numServicio) {
        return serJpa.findServicio(numServicio);
    }

    public void modificarServicio(Servicio ser) {
        serJpa.edit(ser);
    }

    public List<Producto> traerProductos() {
        return prodJpa.findProductoEntities();
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

    public void modificarProducto(Producto prod) {
        prodJpa.edit(prod);
    }


        

    

    

    
        
        
}
