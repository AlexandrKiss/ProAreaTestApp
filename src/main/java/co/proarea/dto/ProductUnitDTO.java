package co.proarea.dto;

import co.proarea.models.ProductUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString @NoArgsConstructor
public class ProductUnitDTO {
    @Getter
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull
    @Getter
    @NotNull (message = "EAN cannot be null")
    private long ean;
    @Getter
    @NotNull (message = "Price cannot be null")
    private double price;
    @Getter
    @NotNull (message = "Product ID cannot be null")
    private Long product;

    public ProductUnitDTO(ProductUnit productUnit) {
        this.name = productUnit.getName();
        this.ean = productUnit.getEan();
        this.price = productUnit.getPrice();
        this.product = productUnit.getProduct().getEan();
    }
}