package co.proarea.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString(exclude="products") @NoArgsConstructor
public class Product {
    @Id @GeneratedValue
    private Long id;

    @Getter @Setter
    @NotNull (message = "Name cannot be null")
    private String name;
    @Getter @Setter
    @NotNull (message = "EAN cannot be null")
    private long ean;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @Getter @Setter
    private List<ProductUnit> products = new ArrayList<>();

    public Product(String name, long ean) {
        this.name = name;
        this.ean = ean;
    }
}