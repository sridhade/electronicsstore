package com.ecommerce.electronicsstore.entity;


import lombok.*;


import javax.persistence.*;


@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String description;


    public Product(String name, Double price, String description) {
        this.name=name;
        this.price=price;
        this.description=description;
    }
}