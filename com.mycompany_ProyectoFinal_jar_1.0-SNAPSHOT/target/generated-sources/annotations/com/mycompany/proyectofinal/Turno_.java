package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Servicio;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-12-20T02:24:52", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Turno.class)
public class Turno_ { 

    public static volatile SingularAttribute<Turno, LocalDateTime> fecha;
    public static volatile SingularAttribute<Turno, Cliente> cliente;
    public static volatile SingularAttribute<Turno, String> estado;
    public static volatile SingularAttribute<Turno, Servicio> servicio;
    public static volatile SingularAttribute<Turno, Integer> id;
    public static volatile SingularAttribute<Turno, String> detalle;

}