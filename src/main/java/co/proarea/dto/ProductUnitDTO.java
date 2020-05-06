package co.proarea.dto;

import co.proarea.models.ProductUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString @NoArgsConstructor
public class ProductUnitDTO {
    @Getter
    private String name;
    @Getter
    private long ean;
    @Getter
    private double price;
    @Getter
        private Long product;

    public ProductUnitDTO(ProductUnit productUnit) {
        this.name = productUnit.getName();
        this.ean = productUnit.getEan();
        this.price = productUnit.getPrice();
        this.product = productUnit.getProduct().getEan();
    }
}