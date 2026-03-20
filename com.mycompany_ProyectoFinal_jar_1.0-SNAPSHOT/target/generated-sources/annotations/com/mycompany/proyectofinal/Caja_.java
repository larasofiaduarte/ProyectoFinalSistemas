package com.mycompany.proyectofinal;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-03-20T17:57:34", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Caja.class)
public class Caja_ { 

    public static volatile SingularAttribute<Caja, LocalDateTime> fecha;
    public static volatile SingularAttribute<Caja, String> tipo;
    public static volatile SingularAttribute<Caja, Double> monto;
    public static volatile SingularAttribute<Caja, Integer> id;
    public static volatile SingularAttribute<Caja, String> medio;
    public static volatile SingularAttribute<Caja, String> detalle;

}