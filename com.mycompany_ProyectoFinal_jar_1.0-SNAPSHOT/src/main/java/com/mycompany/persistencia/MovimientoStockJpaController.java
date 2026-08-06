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

    // Trae solo las SALIDA de un turno que TODAVÍA no fueron revertidas: las posteriores a la
    // última ENTRADA registrada para ese turno (si nunca se revirtió, todas). Los movimientos
    // nunca se borran (ver MovimientoStock, audit trail), así que sin este filtro un turno que
    // se descontó/revirtió más de una vez volvía a reintegrar las SALIDA de ciclos anteriores
    // que ya habían sido compensadas por una ENTRADA — duplicando la devolución de stock.
    public List<MovimientoStock> findSalidasByTurnoId(int turnoId) {
        EntityManager em = emf.createEntityManager();
        try {
            java.time.LocalDateTime ultimaEntrada = em.createQuery(
                "SELECT MAX(m.fecha) FROM MovimientoStock m WHERE m.turnoId = :tid AND m.tipo = 'ENTRADA'",
                java.time.LocalDateTime.class)
                .setParameter("tid", turnoId)
                .getSingleResult();

            String jpql = "SELECT m FROM MovimientoStock m WHERE m.turnoId = :tid AND m.tipo = 'SALIDA'"
                + (ultimaEntrada != null ? " AND m.fecha > :ultimaEntrada" : "")
                + " ORDER BY m.fecha";
            TypedQuery<MovimientoStock> query = em.createQuery(jpql, MovimientoStock.class);
            query.setParameter("tid", turnoId);
            if (ultimaEntrada != null) query.setParameter("ultimaEntrada", ultimaEntrada);
            return query.getResultList();
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
