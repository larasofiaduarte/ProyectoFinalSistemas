package com.mycompany.persistencia;

import com.mycompany.proyectofinal.MovimientoStock;
import java.util.List;
import javax.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovimientoStockJpaController {

    private static final Logger logger = LogManager.getLogger(MovimientoStockJpaController.class);

    private final EntityManagerFactory emf;

    public MovimientoStockJpaController() {
        emf = Persistence.createEntityManagerFactory(
            "com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    public void create(MovimientoStock m) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(m);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Error persistiendo MovimientoStock", e);
            throw new RuntimeException("Error persisting MovimientoStock", e);
        } finally {
            em.close();
        }
    }

    // Trae solo los movimientos de tipo SALIDA de un turno. Se usa para revertir el stock.
    public List<MovimientoStock> findSalidasByTurnoId(int turnoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT m FROM MovimientoStock m WHERE m.turnoId = :tid AND m.tipo = 'SALIDA' ORDER BY m.fecha",
                MovimientoStock.class)
                .setParameter("tid", turnoId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    /** Returns all movements recorded for a given turno (for audit / display). */
    public List<MovimientoStock> findByTurnoId(int turnoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT m FROM MovimientoStock m WHERE m.turnoId = :tid ORDER BY m.fecha",
                MovimientoStock.class)
                .setParameter("tid", turnoId)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
