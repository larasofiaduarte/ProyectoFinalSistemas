package com.mycompany.proyectofinal.util;

/** Tipo de entidad relacionada que un DeleteWarningService debe mencionar en el mensaje. */
public enum RelationType {
    TURNOS("turnos"),
    PRODUCTOS("productos"),
    SERVICIOS("servicios");

    private final String pluralLabel;

    RelationType(String pluralLabel) {
        this.pluralLabel = pluralLabel;
    }

    public String getPluralLabel() {
        return pluralLabel;
    }
}
