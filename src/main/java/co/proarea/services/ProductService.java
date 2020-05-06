package co.proarea.services;

import co.proarea.dto.ProductUnitDTO;
import co.proarea.models.Product;
import co.proarea.models.ProductUnit;

import java.util.List;

public interface ProductService {

    //Product Service
    Product addProduct(Product products);

    List<Product> getAllProduct();

    Product getProduct(long ean);


    //ProductUnit Service
    ProductUnitDTO addProductUnit(ProductUnitDTO productUnitDTO);

    List<ProductUnit> getAllProductUnit();

    ProductUnit getProductUnit(long ean);

    List<ProductUnit> getAllProductUnitByProduct(long ean);

    ProductUnitDTO updateProductUnit(ProductUnitDTO productUnitDTO);

    boolean deleteProductUnit(long ean);
}