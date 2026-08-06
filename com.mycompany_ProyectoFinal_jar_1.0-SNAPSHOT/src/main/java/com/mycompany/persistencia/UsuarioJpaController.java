/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author duart
 */
public class UsuarioJpaController implements Serializable {

    private static final Logger logger = LogManager.getLogger(UsuarioJpaController.class);
    private EntityManagerFactory emf;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public UsuarioJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    // Create
    public void create(Usuario usuario) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error creating user", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public boolean validarUsuarioYEmail(String usuario, String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> q = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :user",
                Usuario.class
            );
            q.setParameter("user", usuario);
            Usuario u;
            try {
                u = q.getSingleResult();
            } catch (NoResultException e) {
                return false;
            }
            return u.getEmail() != null && u.getEmail().trim().equalsIgnoreCase(email.trim());
        } finally {
            em.close();
        }
    }


    // Read
    public Usuario findUsuario(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
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
    public void edit(Usuario usuario) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating user", e);
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
            Usuario usuario = em.getReference(Usuario.class, id);
            em.remove(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting user", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public boolean doesUsernameExist(String username) {
        EntityManager em = emf.createEntityManager(); // Create the EntityManager from the EntityManagerFactory
        try {
            Query query = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", username);

            // If the count is greater than 0, the username exists
            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            logger.error("Error verificando username", e);
            return false;
        } finally {
            if (em != null) {
                em.close(); // Ensure the EntityManager is closed properly
            }
        }
    }

    // excludeId=-1 para altas (no excluye ningún usuario); en modificación se pasa el id del usuario
    // que se está editando para no rechazar su propio email sin cambios.
    public boolean doesEmailExist(String email, int excludeId) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.email) = LOWER(:email) AND u.id <> :excludeId");
            query.setParameter("email", email);
            query.setParameter("excludeId", excludeId);

            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            logger.error("Error verificando email", e);
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Usuario findUsuarioByUsername(String username) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.username = :username",
            Usuario.class
        );
        query.setParameter("username", username);
        return query.getSingleResult();
    } catch (NoResultException e) {
        return null; // username does not exist
    } finally {
        em.close();
    }
}
    
    public boolean checkIfReferenced(int usuId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL to count how many Servicio entries reference the given Usuario
            String query = "SELECT COUNT(s) FROM Servicio s WHERE s.empleado.id = :usuarioId";
            Long count = (Long) em.createQuery(query)
                                  .setParameter("usuarioId", usuId)
                                  .getSingleResult();
            return count > 0; // Returns true if any Servicio references this Usuario
        } finally {
            em.close();
        }
    }

    // Turno.empleado (empleado_id) es la asignación real de un turno a un empleado — mismo
    // patrón que ClienteJpaController.checkIfClientReferenced / ServicioJpaController.checkIfReferenced
    // (cuenta TODOS los turnos, sin filtrar por estado).
    public boolean checkIfReferencedByTurnos(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "SELECT COUNT(t) FROM Turno t WHERE t.empleado.id = :usuarioId";
            Long count = (Long) em.createQuery(query)
                                  .setParameter("usuarioId", usuarioId)
                                  .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Empleado sin turnos: antes de borrarlo hay que desvincular los Servicio que tenía
     * asignados (Servicio.empleado admite null) para no romper la FK — mismo motivo por el
     * que ServicioJpaController ya tenía nullifyEmpleado, solo que acá corre en la misma
     * transacción que el borrado en vez de como paso separado.
     */
    public void destroyAndUnlinkServicios(int usuarioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("UPDATE Servicio s SET s.empleado = null WHERE s.empleado.id = :id")
                .setParameter("id", usuarioId)
                .executeUpdate();

            Usuario usuario = em.getReference(Usuario.class, usuarioId);
            em.remove(usuario);

            tx.commit();
            emf.getCache().evict(Servicio.class);
            emf.getCache().evict(Usuario.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error desvinculando servicios y eliminando empleado", e);
            throw new RuntimeException("Error unlinking servicios and deleting empleado", e);
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Elimina físicamente TODOS los turnos asignados a este empleado, desvincula sus Servicio
     * y borra el empleado — todo en una única transacción. Mismo patrón que
     * ClienteJpaController.deleteAndRemoveTurnos / ServicioJpaController.deleteAndRemoveTurnos.
     */
    public void deleteAndRemoveTurnos(int usuarioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("DELETE FROM Turno t WHERE t.empleado.id = :id")
                .setParameter("id", usuarioId)
                .executeUpdate();

            em.createQuery("UPDATE Servicio s SET s.empleado = null WHERE s.empleado.id = :id")
                .setParameter("id", usuarioId)
                .executeUpdate();

            Usuario usuario = em.getReference(Usuario.class, usuarioId);
            em.remove(usuario);

            tx.commit();
            // El DELETE/UPDATE por JPQL van directo a la base y no invalidan el caché compartido
            // de EclipseLink por sí solos — mismo motivo que en Cliente/ServicioJpaController.
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Servicio.class);
            emf.getCache().evict(Usuario.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error eliminando turnos y empleado", e);
            throw new RuntimeException("Error deleting turnos and empleado", e);
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Reasigna los turnos y servicios de este empleado a otro empleado y borra el original —
     * todo en una única transacción, mismo motivo que deleteAndRemoveTurnos.
     */
    public void deleteAndReassignTurnos(int usuarioId, int nuevoUsuarioId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Usuario nuevo = em.getReference(Usuario.class, nuevoUsuarioId);

            em.createQuery("UPDATE Turno t SET t.empleado = :nuevo WHERE t.empleado.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", usuarioId)
                .executeUpdate();

            em.createQuery("UPDATE Servicio s SET s.empleado = :nuevo WHERE s.empleado.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", usuarioId)
                .executeUpdate();

            Usuario usuario = em.getReference(Usuario.class, usuarioId);
            em.remove(usuario);

            tx.commit();
            emf.getCache().evict(Turno.class);
            emf.getCache().evict(Servicio.class);
            emf.getCache().evict(Usuario.class);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error reasignando turnos y eliminando empleado", e);
            throw new RuntimeException("Error reassigning turnos and deleting empleado", e);
        } finally {
            if (em != null) em.close();
        }
    }
}
