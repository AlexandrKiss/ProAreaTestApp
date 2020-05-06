package co.proarea.controllers;

import co.proarea.dto.ProductUnitDTO;
import co.proarea.models.Product;
import co.proarea.models.ProductUnit;
import co.proarea.pdf.Invoice;
import co.proarea.pdf.models.InvoiceRow;
import co.proarea.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/product/")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("unit/add")
    public ProductUnitDTO addProductUnit(@RequestBody ProductUnitDTO productUnitDTO){
        try {
            return productService.addProductUnit(productUnitDTO);
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "Product Unit EAN '" + productUnitDTO.getEan() + "' already exists");
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product with EAN: '" + productUnitDTO.getProduct() + "'");
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("unit/update")
    public ProductUnitDTO updateProduct(@RequestBody ProductUnitDTO productUnitDTO){
        try {
            return productService.updateProductUnit(productUnitDTO);
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "Product Unit EAN '" + productUnitDTO.getEan() + "' already exists");
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product with EAN: '" + productUnitDTO.getProduct() + "'");
        }
    }

    @PostMapping("unit/{ean}")
    public ProductUnit getProductUnit(@PathVariable("ean") long ean){
        try {
            return productService.getProductUnit(ean);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Unit with EAN: '" + ean + "'");
        }
    }

    @PostMapping("unit/list")
    public List<ProductUnit> getUnitList(){
        return productService.getAllProductUnit();
    }

    @PostMapping("unit/list/{ean}")
    public List<ProductUnit> getUnitListByProduct(@PathVariable("ean") Long ean){
        return productService.getAllProductUnitByProduct(ean);
    }

    @PostMapping("unit/invoice")
    public ResponseEntity<byte[]> newInvoice(@RequestBody ArrayList<InvoiceRow> invoiceRows) throws IOException {
        try {
            Invoice invoice = new Invoice(productService, invoiceRows);
            return invoice.toResponseEntity();
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "One of Units not found");
        }
    }

    @PostMapping("group/{ean}")
    public Product getProduct(@PathVariable("ean") long ean){
        try {
            return productService.getProduct(ean);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product with EAN: '" + ean + "'");
        }
    }
}