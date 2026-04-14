package com.minicoy.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product; // Which product was bought?

    private Integer quantity;
    private Double price; // Price at the time of purchase

    @ManyToOne
    @JsonIgnore // To prevent the infinite loop we talked about!
    private Order order; // Which order does this line belong to?
}
