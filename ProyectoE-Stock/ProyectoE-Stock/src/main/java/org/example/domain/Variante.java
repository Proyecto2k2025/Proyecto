package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "variantes")
@Getter
@Setter
public class Variante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "talla", length = 12)
    private String talla;

    @Column(name = "color", length = 45)
    private String color;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "sku", length = 45)
    private String sku;

    @Column(name = "stock")
    private Integer stock;


    @ManyToOne
    @JoinColumn(name = "productos_id", nullable = false)
    private Producto producto;
}
