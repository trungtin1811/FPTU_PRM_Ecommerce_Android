
package com.example.ecommerceproject.dto;

import com.example.ecommerceproject.entities.Product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel implements Serializable {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Double price;

    public ProductModel(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }
}
