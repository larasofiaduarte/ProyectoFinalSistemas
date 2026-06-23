/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Turno;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.*;
import com.mycompany.proyectofinal.Servicio;

/**
 *
 * @author duart
 */
public class TurnoJpaController {
    private EntityManagerFactory emf;

    public TurnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TurnoJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    // Create
    public void create(Turno turno) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(turno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error cargando turno", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Turno findTurno(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Turno.class, id);
        } finally {
            em.close();
        }
    }

    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }
    
    public List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Turno> query = em.createQuery("SELECT u FROM Turno u", Turno.class);
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
    public void edit(Turno turno) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(turno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error actualizando turno", e);
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
            Turno turno = em.getReference(Turno.class, id);
            em.remove(turno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar turno", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public boolean turnoYaExiste(Servicio servicio, LocalDateTime fecha) {
        EntityManager em = emf.createEntityManager();
            try {
            // Create query to check if there exists a Turno with the same service and date, excluding the current appointment
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Turno t WHERE t.servicio = :servicio AND t.fecha = :fecha", Long.class);
            query.setParameter("servicio", servicio);
            query.setParameter("fecha", fecha);

            // Devuelve true si el conteo es mayor que 0, lo que indica que ya existe un turno con estos valores
            return query.getSingleResult() > 0;
        } finally {
            em.close();
    }
            
            
}
    
    
    //para que no puedan haber 2 turnos al mismo tiempo
    public boolean turnoYaExiste2(Servicio servicio, LocalDateTime fecha, int currentId) {
        EntityManager em = emf.createEntityManager();
            try {
            // Create query to check if there exists a Turno with the same service and date, excluding the current appointment
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Turno t WHERE t.servicio = :servicio AND t.fecha = :fecha AND t.id != :currentTurnoId", Long.class);
            query.setParameter("servicio", servicio);
            query.setParameter("fecha", fecha);
            query.setParameter("currentTurnoId", currentId);

            // Devuelve true si el conteo es mayor que 0, lo que indica que ya existe un turno con estos valores
            return query.getSingleResult() > 0;
        } finally {
            em.close();
    }
    
    }
    
    public List<Turno> findByCliente(int clienteId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT t FROM Turno t WHERE t.cliente.id = :id", Turno.class)
                .setParameter("id", clienteId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Turno> findActiveByServicio(int servicioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT t FROM Turno t WHERE t.servicio.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')",
                Turno.class)
                .setParameter("id", servicioId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public void cancelByServicio(int servicioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(
                "UPDATE Turno t SET t.estado = 'CANCELADO' WHERE t.servicio.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')")
                .setParameter("id", servicioId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error cancelling turnos by servicio", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void reassignServicio(int fromServiceId, int toServiceId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Servicio nuevo = em.getReference(Servicio.class, toServiceId);
            em.createQuery(
                "UPDATE Turno t SET t.servicio = :nuevo WHERE t.servicio.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", fromServiceId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error reassigning servicio on turnos", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Turno> findActiveByCliente(int clienteId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT t FROM Turno t WHERE t.cliente.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')",
                Turno.class)
                .setParameter("id", clienteId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public void cancelByCliente(int clienteId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(
                "UPDATE Turno t SET t.estado = 'CANCELADO' WHERE t.cliente.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')")
                .setParameter("id", clienteId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error cancelling turnos by cliente", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Turno> findActiveByEmpleado(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT t FROM Turno t WHERE t.servicio.empleado.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')",
                Turno.class)
                .setParameter("id", usuarioId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Retorna turnos activos de un empleado en una fecha dada, excluyendo el turno con excludeId (usa -1 para no excluir ninguno)
    public List<Turno> findByEmpleadoAndFecha(int empleadoId, LocalDate fecha, int excludeId) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin    = fecha.plusDays(1).atStartOfDay();
            return em.createQuery(
                "SELECT t FROM Turno t WHERE t.empleado.id = :empId" +
                " AND t.fecha >= :inicio AND t.fecha < :fin" +
                " AND t.estado NOT IN ('CANCELADO','Cancelado')" +
                " AND t.id != :excludeId",
                Turno.class)
                .setParameter("empId",     empleadoId)
                .setParameter("inicio",    inicio)
                .setParameter("fin",       fin)
                .setParameter("excludeId", excludeId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public void cancelByEmpleado(int usuarioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(
                "UPDATE Turno t SET t.estado = 'CANCELADO' WHERE t.servicio.empleado.id = :id AND t.estado NOT IN ('CANCELADO','Cancelado','Finalizado')")
                .setParameter("id", usuarioId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error cancelling turnos by empleado", e);
        } finally {
            if (em != null) em.close();
        }
    }
}
