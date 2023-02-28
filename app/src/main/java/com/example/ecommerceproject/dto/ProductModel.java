
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
    public Long id;
    public String name;
    public String imageUrl;

    public ProductModel(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
    }
}
