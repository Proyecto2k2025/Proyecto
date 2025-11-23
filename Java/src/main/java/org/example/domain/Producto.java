package org.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "categoria", length = 45)
    private String categoria;

    @Column(name = "material", length = 45)
    private String material;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Variante> variantes;
}
