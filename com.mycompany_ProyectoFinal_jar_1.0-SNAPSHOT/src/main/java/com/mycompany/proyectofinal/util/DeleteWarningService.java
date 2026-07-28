package com.mycompany.proyectofinal.util;

import java.util.List;

/**
 * Genera el texto de los diálogos de confirmación de borrado, en un único lugar,
 * para que todas las entidades usen la misma redacción (formal, sin detalle técnico)
 * en vez de cada handler escribiendo su propio mensaje ad hoc.
 */
public final class DeleteWarningService {

    private static final String ACCION_DEFAULT = "eliminar";
    private static final String ACCION_REFLEXIVA_DEFAULT = "se elimina";
    private static final String PREGUNTA_DEFAULT = "¿Desea continuar?";

    private DeleteWarningService() {}

    /** Caso estándar: acción "eliminar", pregunta de cierre genérica. */
    public static String buildMessage(EntityType type, List<RelationType> relations) {
        return buildMessage(type, relations, PREGUNTA_DEFAULT);
    }

    /** Permite personalizar la pregunta de cierre (ej. cuando el diálogo ofrece elegir qué hacer). */
    public static String buildMessage(EntityType type, List<RelationType> relations, String followUpQuestion) {
        return buildMessage(type, relations, ACCION_DEFAULT, ACCION_REFLEXIVA_DEFAULT, followUpQuestion);
    }

    /**
     * Forma completa: permite además personalizar el verbo de la acción (ej. "dar de baja" /
     * "se da de baja" para entidades con soft-delete, en vez de "eliminar" / "se elimina").
     */
    public static String buildMessage(EntityType type, List<RelationType> relations,
                                       String accionInfinitivo, String accionReflexiva, String followUpQuestion) {
        if (relations == null || relations.isEmpty()) {
            return "¿Desea " + accionInfinitivo + " este registro?";
        }
        String listado = joinNatural(relations);
        return "Este " + type.getLabel() + " tiene " + listado + " asociados. "
             + "Si " + accionReflexiva + ", podría ocasionar la pérdida de información relacionada. "
             + followUpQuestion;
    }

    private static String joinNatural(List<RelationType> relations) {
        List<String> labels = relations.stream().map(RelationType::getPluralLabel).toList();
        if (labels.size() == 1) return labels.get(0);
        String todosMenosUltimo = String.join(", ", labels.subList(0, labels.size() - 1));
        return todosMenosUltimo + " y " + labels.get(labels.size() - 1);
    }
}
