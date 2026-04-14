package com.minicoy.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // e.g., "Hot Beverages"

    @OneToMany(mappedBy = "category")
    @JsonIgnore // This tells Spring: "Don't follow the link back to products when making JSON"
    private List<Product> products; // This is the 3rd ingredient
}
