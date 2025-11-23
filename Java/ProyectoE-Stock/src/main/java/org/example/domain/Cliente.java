package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Setter
@Getter


public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 45, unique = true)
    private String nombre;

    @Column(name = "email", nullable = false, length = 45, unique = true)
    private String email;

    @Column(name = "telefono", nullable = false, length = 45, unique = true)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 45, unique = true)
    private String direccion;

    //al eliminar un cliente se eliminen sus pedidos
    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();

}
