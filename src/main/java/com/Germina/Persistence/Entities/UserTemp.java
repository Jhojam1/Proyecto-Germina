package com.Germina.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Temporal_Usuarios")
public class UserTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombres_Completos")
    private String fullName;

    @Column(name = "Numero_Identificacion", unique = true)
    private Long numberIdentification;

    @Column(name = "Estado")
    private String state;

    @Column(name = "Correo", unique = true)
    private String mail ;

}
