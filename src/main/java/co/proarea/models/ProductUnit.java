package co.proarea.models;

import co.proarea.dto.ProductUnitDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ProductUnit {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private long ean;

    private double price;

    @JsonManagedReference
    @ManyToOne
    private Product product;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "productUnit")
    private List<DBFile> dbFiles = new ArrayList<>();

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