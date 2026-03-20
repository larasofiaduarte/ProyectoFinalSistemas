package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.Proveedor;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-03-20T17:57:34", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, Double> minimo;
    public static volatile SingularAttribute<Producto, Proveedor> Proveedor;
    public static volatile SingularAttribute<Producto, Integer> id;
    public static volatile SingularAttribute<Producto, Double> stock;
    public static volatile SingularAttribute<Producto, String> nombre;

}