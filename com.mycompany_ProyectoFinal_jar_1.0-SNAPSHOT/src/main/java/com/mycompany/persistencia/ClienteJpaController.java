/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author duart
 */
public class ClienteJpaController implements Serializable {

    private static final Logger logger = LogManager.getLogger(ClienteJpaController.class);
    private EntityManagerFactory emf;

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ClienteJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    // Create
    public void create(Cliente cliente) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error cargando cliente", e);
            throw new RuntimeException("Error cargando cliente", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Cliente findCliente(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT u FROM Cliente u", Cliente.class);
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // Update
    public void edit(Cliente cliente) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error actualizando cliente", e);
            throw new RuntimeException("Error actualizando cliente", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Delete
    public void destroy(int id) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            Cliente cliente = em.getReference(Cliente.class, id);
            em.remove(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error eliminando cliente", e);
            throw new RuntimeException("Error al eliminar client", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    /**
     * Elimina físicamente TODOS los turnos que referencian al cliente y borra el cliente —
     * todo en una única transacción. Mismo patrón que ServicioJpaController.deleteAndRemoveTurnos:
     * sin soft-delete, sin NULL, sin estado intermedio si algo falla a mitad de camino.
     */
    public void deleteAndRemoveTurnos(int clienteId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("DELETE FROM Turno t WHERE t.cliente.id = :id")
                .setParameter("id", clienteId)
                .executeUpdate();

            Cliente cliente = em.getReference(Cliente.class, clienteId);
            em.remove(cliente);

            tx.commit();
            // El DELETE por JPQL va directo a la base y no invalida el caché compartido de
            // EclipseLink por sí solo (mismo motivo que en ServicioJpaController).
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Cliente.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error eliminando turnos y cliente", e);
            throw new RuntimeException("Error deleting turnos and cliente", e);
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Reasigna todos los turnos del cliente a otro cliente y borra el cliente original —
     * todo en una única transacción, mismo motivo que deleteAndRemoveTurnos.
     */
    public void deleteAndReassignTurnos(int clienteId, int nuevoClienteId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Cliente nuevo = em.getReference(Cliente.class, nuevoClienteId);
            em.createQuery("UPDATE Turno t SET t.cliente = :nuevo WHERE t.cliente.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", clienteId)
                .executeUpdate();

            Cliente cliente = em.getReference(Cliente.class, clienteId);
            em.remove(cliente);

            tx.commit();
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Cliente.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error reasignando turnos y eliminando cliente", e);
            throw new RuntimeException("Error reassigning turnos and deleting cliente", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public boolean checkIfClientReferenced(int clienteId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL to count how many Turno entries reference the given Servicio
            String query = "SELECT COUNT(t) FROM Turno t WHERE t.cliente.id = :clienteId";
            Long count = (Long) em.createQuery(query)
                                  .setParameter("clienteId", clienteId)
                                  .getSingleResult();
            return count > 0; // Returns true if any Turno references this Cliente
        } finally {
            em.close();
        }
    }
    
}
