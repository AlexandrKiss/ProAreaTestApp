package co.proarea.dto;

import co.proarea.models.ProductUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString @NoArgsConstructor @Getter
public class ProductUnitDTO {
    private String name;
    private long ean;
    private double price;
    private Long product;

    public ProductUnitDTO(ProductUnit productUnit) {
        this.name = productUnit.getName();
        this.ean = productUnit.getEan();
        this.price = productUnit.getPrice();
        this.product = productUnit.getProduct().getEan();
    }
}