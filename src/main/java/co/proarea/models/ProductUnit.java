package co.proarea.models;

import co.proarea.dto.ProductUnitDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@ToString @NoArgsConstructor
public class ProductUnit {
    @Id
    @GeneratedValue
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private long ean;

    @Getter @Setter
    private double price;

    @JsonManagedReference
    @ManyToOne
    @Getter @Setter
    private Product product;

    public ProductUnit(String name, long ean, double price, Product product) {
        this.name = name;
        this.ean = ean;
        this.price = price;
        this.product = product;
    }

    public ProductUnit(ProductUnitDTO puDTO, Product product){
        this.name = puDTO.getName();
        this.ean = puDTO.getEan();
        this.price = puDTO.getPrice();
        this.product = product;
    }
}