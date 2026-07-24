package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.ServicioProducto;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServicioProductoJpaController implements Serializable {

    private static final Logger logger = LogManager.getLogger(ServicioProductoJpaController.class);
    private EntityManagerFactory emf;

    public ServicioProductoJpaController() {
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    public List<ServicioProducto> findByProducto(int productoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT sp FROM ServicioProducto sp WHERE sp.producto.id = :id",
                ServicioProducto.class)
                .setParameter("id", productoId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteByProducto(int productoId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM ServicioProducto sp WHERE sp.producto.id = :id")
                .setParameter("id", productoId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error deleting ServicioProducto by producto", e);
            throw new RuntimeException("Error deleting ServicioProducto by producto", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void replaceProducto(int fromProductoId, int toProductoId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Producto nuevo = em.getReference(Producto.class, toProductoId);
            em.createQuery(
                "UPDATE ServicioProducto sp SET sp.producto = :nuevo WHERE sp.producto.id = :id")
                .setParameter("nuevo", nuevo)
                .setParameter("id", fromProductoId)
                .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            logger.error("Error replacing producto in ServicioProducto", e);
            throw new RuntimeException("Error replacing producto in ServicioProducto", e);
        } finally {
            if (em != null) em.close();
        }
    }
}
