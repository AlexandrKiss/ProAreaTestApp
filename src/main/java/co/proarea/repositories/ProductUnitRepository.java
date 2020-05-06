package co.proarea.repositories;

import co.proarea.models.Product;
import co.proarea.models.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
    ProductUnit findByEan(long ean);
    List<ProductUnit> getByProduct(Product product);
    void deleteByEan(long ean);
}
