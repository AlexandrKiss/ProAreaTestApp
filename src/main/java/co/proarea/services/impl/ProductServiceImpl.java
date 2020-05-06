package co.proarea.services.impl;

import co.proarea.dto.ProductUnitDTO;
import co.proarea.models.Product;
import co.proarea.models.ProductUnit;
import co.proarea.repositories.ProductRepository;
import co.proarea.repositories.ProductUnitRepository;
import co.proarea.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductUnitRepository productUnitRepository) {
        this.productRepository = productRepository;
        this.productUnitRepository = productUnitRepository;
    }

    //Product Service
    @Override
    @Transactional
    public Product addProduct(Product product){
        long ean = product.getEan();
        if (productRepository.findByEan(ean) != null) {
            log.warn("IN addProduct - a product EAN {} already exists", ean);
            throw new IllegalArgumentException();
        }
        log.info("IN addProduct - product: {} successfully added", product);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProduct() {
        List<Product> result = productRepository.findAll();
        log.info("IN getAllProduct - {} products found", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(long ean){
        Product result = productRepository.findByEan(ean);
        if(result == null) {
            log.warn("IN getProduct - no product found by EAN: {}", ean);
            throw new NullPointerException();
        }
        log.info("IN getProduct - product: {}", result);
        return result;
    }

    //ProductUnit Service
    @Override
    @Transactional
    public ProductUnitDTO addProductUnit(ProductUnitDTO productUnitDTO){
        long ean = productUnitDTO.getEan();
        if (productUnitRepository.findByEan(ean) != null) {
            log.warn("IN addProduct - product unit EAN {} already exists", ean);
            throw new IllegalArgumentException();
        }
        Product product =  this.getProduct(productUnitDTO.getProduct());
        ProductUnit productUnit = productUnitRepository.save(
                new ProductUnit(productUnitDTO, product));
        log.info("IN addProductUnit - productUnit: {} successfully added", productUnit);
        return new ProductUnitDTO(productUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnit> getAllProductUnit() {
        List<ProductUnit> result = productUnitRepository.findAll();
        log.info("IN getAllProductUnit - {} product units found", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductUnit getProductUnit(long ean){
        ProductUnit result = productUnitRepository.findByEan(ean);
        if(result == null) {
            log.warn("IN getProductUnit - no product unit found by EAN: {}", ean);
            throw new NullPointerException();
        }
        log.info("IN getProductUnit - product unit: {}", result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnit> getAllProductUnitByProduct(long ean){
        Product product = productRepository.findByEan(ean);
        if(product == null) {
            log.warn("IN getAllProductUnitByProduct - no product found by EAN: {}", ean);
            throw new NullPointerException();
        }
        List<ProductUnit> result = productUnitRepository.getByProduct(product);
        log.info("IN getAllProductUnitByProduct - {} product units found", result.size());
        return result;
    }

    @Override
    @Transactional
    public ProductUnitDTO updateProductUnit(ProductUnitDTO productUnitDTO){
        ProductUnit productUnit = productUnitRepository.findByEan(productUnitDTO.getEan());
        if (productUnit == null) {
            log.warn("IN updateProductUnit - no product unit found by EAN: {}", productUnitDTO.getEan());
            throw new NullPointerException();
        }
        log.info("IN updateProductUnit - productUnit: {} successfully updated", productUnit);
        return new ProductUnitDTO(productUnitRepository.save(productUnit));
    }

    @Override
    @Transactional
    public boolean deleteProductUnit(long ean){
        ProductUnit result = productUnitRepository.findByEan(ean);
        if (result == null) {
            log.warn("IN deleteProductUnit - no product unit found by EAN: {}", ean);
            throw new NullPointerException();
        }
        productUnitRepository.deleteByEan(ean);
        log.info("IN deleteProductUnit - productUnit with EAN: {} successfully deleted", ean);
        return true;
    }
}
