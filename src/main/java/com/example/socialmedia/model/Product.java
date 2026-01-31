package com.example.socialmedia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;

    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
