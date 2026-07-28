package com.mycompany.proyectofinal.util;

/** Entidad principal sobre la que se está confirmando una eliminación. */
public enum EntityType {
    SERVICIO("servicio"),
    CLIENTE("cliente"),
    EMPLEADO("empleado"),
    PROVEEDOR("proveedor"),
    PRODUCTO("producto");

    private final String label;

    EntityType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
