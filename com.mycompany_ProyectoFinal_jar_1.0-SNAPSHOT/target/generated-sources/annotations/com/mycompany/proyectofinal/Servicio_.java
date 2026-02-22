package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-02-22T02:04:04", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Servicio.class)
public class Servicio_ { 

    public static volatile SingularAttribute<Servicio, String> precio;
    public static volatile SingularAttribute<Servicio, Usuario> empleado;
    public static volatile ListAttribute<Servicio, Turno> turnos;
    public static volatile SingularAttribute<Servicio, Integer> id;
    public static volatile SingularAttribute<Servicio, String> nombre;
    public static volatile ListAttribute<Servicio, ServicioProducto> productos;

}