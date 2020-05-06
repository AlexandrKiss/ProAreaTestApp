package co.proarea.repositories;

import co.proarea.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {
    Product findByEan(long ean);
    void deleteByEan(long ean);
}