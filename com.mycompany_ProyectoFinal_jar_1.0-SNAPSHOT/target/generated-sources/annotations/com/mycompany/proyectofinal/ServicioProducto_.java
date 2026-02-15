package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.Servicio;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-02-13T15:18:05", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(ServicioProducto.class)
public class ServicioProducto_ { 

    public static volatile SingularAttribute<ServicioProducto, Servicio> servicio;
    public static volatile SingularAttribute<ServicioProducto, Integer> id;
    public static volatile SingularAttribute<ServicioProducto, Producto> producto;
    public static volatile SingularAttribute<ServicioProducto, Double> cantidadUsada;

}