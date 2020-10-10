package co.proarea.controllers;

import co.proarea.dto.ProductUnitDTO;
import co.proarea.models.InvoiceRow;
import co.proarea.models.Product;
import co.proarea.models.ProductUnit;
import co.proarea.models.User;
import co.proarea.pdf.Invoice;
import co.proarea.services.ProductService;
import co.proarea.services.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/product/")
@Api(value="onlinestore", description="Operations pertaining to products in Online Store")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("unit/add")
    @ApiOperation(value = "Add a product unit (ROLE_ADMIN)", response = ProductUnitDTO.class)
    public ProductUnitDTO addProductUnit(@RequestBody ProductUnitDTO productUnitDTO){
        try {
            return productService.addProductUnit(productUnitDTO);
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "Product Unit EAN '" + productUnitDTO.getEan() + "' already exists");
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("unit/update")
    @ApiOperation(value = "Update a product unit (ROLE_ADMIN)",response = ProductUnitDTO.class)
    public ProductUnitDTO updateProductUnit(@RequestBody ProductUnitDTO productUnitDTO){
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

    @GetMapping("unit/{ean}")
    @ApiOperation(value = "Search a product unit with an EAN (ROLE_ADMIN, ROLE_USER)",response = ProductUnit.class)
    public ProductUnit getProductUnit(@PathVariable("ean") long ean){
        try {
            return productService.getProductUnit(ean);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Unit with EAN: '" + ean + "'");
        }
    }

    @GetMapping("unit/list")
    @ApiOperation(value = "Get a list of available product units (ROLE_ADMIN, ROLE_USER)",
            response = ProductUnit.class,
            responseContainer = "List")
    public List<ProductUnit> getUnitList(){
        return productService.getAllProductUnit();
    }

    @GetMapping("unit/list/{ean}")
    @ApiOperation(value = "Get a list of ProductUnit by Product (ROLE_ADMIN, ROLE_USER)",
            response = ProductUnit.class,
            responseContainer = "List")
    public List<ProductUnit> getUnitListByProduct(@PathVariable("ean") Long ean){
        return productService.getAllProductUnitByProduct(ean);
    }

    @GetMapping("unit/invoice")
    @ApiOperation(value = "Get invoice in PDF format (ROLE_ADMIN, ROLE_USER)", response = ResponseEntity.class)
    public ResponseEntity<byte[]> newInvoice(@RequestBody ArrayList<InvoiceRow> invoiceRows) throws IOException {
        try {
            Invoice invoice = new Invoice(productService, invoiceRows);
            return invoice.toResponseEntity();
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "One of Units not found");
        }
    }

    @GetMapping("group/{ean}")
    @ApiOperation(value = "Search a product with an EAN (ROLE_ADMIN, ROLE_USER)",response = Product.class)
    public Product getProduct(@PathVariable("ean") long ean){
        try {
            return productService.getProduct(ean);
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Product with EAN: '" + ean + "'");
        }
    }
}