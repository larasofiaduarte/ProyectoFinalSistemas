package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationService {

    private static NotificationService instance;

    private final ProductoJpaController productoController;
    private List<Producto> cachedLowStock = new ArrayList<>();
    private final List<Runnable> changeListeners = new ArrayList<>();

    private NotificationService() {
        this.productoController = new ProductoJpaController();
        refreshLowStock();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void refreshLowStock() {
        cachedLowStock = productoController.findLowStock();
    }

    public void notifyStockChange() {
        refreshLowStock();
        for (Runnable listener : new ArrayList<>(changeListeners)) {
            listener.run();
        }
    }

    /** Adds a listener; called once per TopMenu instance. */
    public void setOnChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public List<Producto> getCachedLowStock() {
        return Collections.unmodifiableList(cachedLowStock);
    }

    public boolean hasNotifications() {
        return !cachedLowStock.isEmpty();
    }
}
