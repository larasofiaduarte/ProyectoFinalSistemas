package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Categoria;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoriaJpaController implements Serializable {

    private static final Logger logger = LogManager.getLogger(CategoriaJpaController.class);
    private EntityManagerFactory emf;

    public CategoriaJpaController() {
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    public void create(Categoria categoria) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.persist(categoria);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            // Detecta violación de UNIQUE KEY (nombre duplicado)
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                    throw new IllegalStateException("La categoría ya existe", e);
                }
                cause = cause.getCause();
            }
            logger.error("Error creando categoria", e);
            throw new RuntimeException("Error creando categoria", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public Categoria find(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public List<Categoria> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nombre", Categoria.class)
                     .getResultList();
        } catch (Exception e) {
            logger.error("Error buscando categorias — tabla ausente o BD no disponible", e);
            return java.util.Collections.emptyList();
        } finally {
            em.close();
        }
    }

    // Inserta categorías base si la tabla está vacía (solo se ejecuta en primer inicio)
    public void seedDefaults() {
        try {
            if (!findAll().isEmpty()) return;
            String[][] defaults = {
                {"Shampoo",        "ml"},
                {"Acondicionador", "ml"},
                {"Tintura",        "ml"},
                {"Tratamiento",    "ml"},
                {"Styling",        "ml"},
                {"Herramientas",   "unidades"},
                {"Otros",          "ml"}
            };
            for (String[] d : defaults) {
                create(new Categoria(d[0], d[1]));
            }
            logger.info("Categorías por defecto insertadas ({} registros)", defaults.length);
        } catch (Exception e) {
            logger.warn("No se pudieron insertar categorías por defecto: {}", e.getMessage());
        }
    }

    public void edit(Categoria categoria) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.merge(categoria);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error actualizando categoria", e);
            throw new RuntimeException("Error actualizando categoria", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void destroy(int id) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Categoria c = em.getReference(Categoria.class, id);
            em.remove(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error eliminando categoria id={}", id, e);
            throw new RuntimeException("Error eliminando categoria", e);
        } finally {
            if (em != null) em.close();
        }
    }
}
