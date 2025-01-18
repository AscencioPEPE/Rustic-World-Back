package com.rusticworld.app.controller;

import com.rusticworld.app.dto.ProductDTO;
import com.rusticworld.app.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;

    @GetMapping("/all")
    @Operation(summary = "List all products", description = "Retrieves a list of products, optionally filtered by categories, sizes, and sorted by price.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of products")
    public ResponseEntity<Object> findAll(
            @RequestParam(required = false) @Parameter(description = "List of categories to filter", example = "electronics, books") List<String> categories,
            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Order of price sorting, 'asc' or 'desc'", example = "asc") String priceOrder,
            @RequestParam @Parameter(description = "Number of results per page", example = "10") Integer limit,
            @RequestParam @Parameter(description = "Page number", example = "1") Integer page,
            @RequestParam(required = false) @Parameter(description = "sku prefix to filter products whose names start with these letters", example = "Jo") String namePrefix
    ) {
        return productService.getAll(categories, priceOrder, limit, page, namePrefix);
    }

    @PostMapping()
    @Operation(summary = "Create a new product", description = "Creates a new product in the system.")
    @ApiResponse(responseCode = "200", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Product not created successfully")
    public ResponseEntity<Object> save(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @DeleteMapping()
    @Operation(summary = "Delete a product", description = "Deletes a product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    public ResponseEntity<Object> delete(@RequestParam Long sku) {
        return productService.delete(sku);
    }

    @GetMapping()
    @Operation(summary = "Get a product by SKU", description = "Retrieves a single product by its SKU.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> getProductBySku(
            @RequestParam @Parameter(description = "SKU of the product to retrieve", example = "12345") Long sku) {
        return productService.get(sku);
    }

    @PutMapping()
    @Operation(summary = "Update an existing product", description = "Updates an existing product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> updateProduct(
            @RequestParam("skuExisting") Long skuExisting,
            @RequestBody ProductDTO productDTO) {
        return productService.update(skuExisting,productDTO);
    }


}
