/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.Turno;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author duart
 */
public class ServicioJpaController implements Serializable {

    private static final Logger logger = LogManager.getLogger(ServicioJpaController.class);
    private EntityManagerFactory emf;

    public ServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ServicioJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    
    // Create
    public void create(Servicio servicio) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error cargando servicio", e);
            throw new RuntimeException("Error cargando servicio", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Servicio findServicio(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Servicio> query = em.createQuery("SELECT u FROM Servicio u", Servicio.class);
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
    public void edit(Servicio servicio) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error actualizando servicio", e);
            throw new RuntimeException("Error updating servicio", e);
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
            Servicio servicio = em.getReference(Servicio.class, id);
            em.remove(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error eliminando servicio", e);
            throw new RuntimeException("Error deleting servicio", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina físicamente TODOS los turnos que referencian el servicio y borra el servicio —
     * todo en una única transacción. No se usa NULL en ningún momento: Turno.servicio es NOT NULL
     * (tanto en la entidad como en la base) y esta operación lo respeta borrando las filas en vez
     * de desvincularlas.
     */
    public void deleteAndRemoveTurnos(int servicioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("DELETE FROM Turno t WHERE t.servicio.id = :id")
                .setParameter("id", servicioId)
                .executeUpdate();

            Servicio servicio = em.getReference(Servicio.class, servicioId);
            em.remove(servicio);

            tx.commit();
            // El DELETE por JPQL va directo a la base y no invalida el caché compartido de
            // EclipseLink por sí solo — sin este evict, una lectura posterior (incluso desde otro
            // EntityManager) puede devolver instancias de Turno ya borradas desde el caché.
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Servicio.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error eliminando turnos y servicio", e);
            throw new RuntimeException("Error deleting turnos and servicio", e);
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Reasigna todos los turnos del servicio a otro servicio y borra el original —
     * todo en una única transacción, mismo motivo que deleteAndCancelTurnos.
     */
    public void deleteAndReassignTurnos(int servicioId, int nuevoServicioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Servicio nuevo = em.getReference(Servicio.class, nuevoServicioId);
            em.createQuery("UPDATE Turno t SET t.servicio = :nuevo WHERE t.servicio.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", servicioId)
                .executeUpdate();

            Servicio servicio = em.getReference(Servicio.class, servicioId);
            em.remove(servicio);

            tx.commit();
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Servicio.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error reasignando turnos y eliminando servicio", e);
            throw new RuntimeException("Error reassigning turnos and deleting servicio", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Servicio> findByEmpleado(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT s FROM Servicio s WHERE s.empleado.id = :id", Servicio.class)
                .setParameter("id", usuarioId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public void nullifyEmpleado(int usuarioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery("UPDATE Servicio s SET s.empleado = null WHERE s.empleado.id = :id")
                .setParameter("id", usuarioId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error nullifying empleado on servicios", e);
            throw new RuntimeException("Error nullifying empleado on servicios", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void reassignEmpleado(int fromId, int toId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            com.mycompany.proyectofinal.Usuario nuevo =
                em.getReference(com.mycompany.proyectofinal.Usuario.class, toId);
            em.createQuery("UPDATE Servicio s SET s.empleado = :nuevo WHERE s.empleado.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", fromId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error reassigning empleado on servicios", e);
            throw new RuntimeException("Error reassigning empleado on servicios", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public boolean checkIfReferenced(int servicioId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL to count how many Turno entries reference the given Servicio
            String query = "SELECT COUNT(t) FROM Turno t WHERE t.servicio.id = :servicioId";
            Long count = (Long) em.createQuery(query)
                                  .setParameter("servicioId", servicioId)
                                  .getSingleResult();
            return count > 0; // Returns true if any Turno references this Servicio
        } finally {
            em.close();
        }
    }
    
    
}
