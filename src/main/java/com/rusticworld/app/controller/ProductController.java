package com.rusticworld.app.controller;

import com.rusticworld.app.dto.ProductDTO;
import com.rusticworld.app.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam @Parameter(description = "Page number", example = "1") Integer page
    ) {
        return productService.getAll(categories, priceOrder, limit, page);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Create a new product", description = "Creates a new product in the system.")
    @ApiResponse(responseCode = "200", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Product not created successfully")
    public ResponseEntity<Object> save(@RequestParam("name") String name,
                                       @RequestParam("sku") String sku,
                                       @RequestParam("category") String category,
                                       @RequestParam("description") String description,
                                       @RequestParam("size") String size,
                                       @RequestParam("weight") String weight,
                                       @RequestParam("price") Double price,
                                       @RequestParam("priceUnitary") Double priceUnitary,
                                       @RequestParam("priceWholesale") Double priceWholesale,
                                       @RequestParam("quantity") Integer quantity,
                                       @RequestParam("image") MultipartFile image) {
        return productService.save(ProductDTO.builder()
                .name(name)
                .sku(sku)
                .category(category)
                .description(description)
                .size(size)
                .weight(weight)
                .price(price)
                .priceUnitary(priceUnitary)
                .priceWholesale(priceWholesale)
                .quantity(quantity)
                .image(image)
                .build());
    }

    @DeleteMapping()
    @Operation(summary = "Delete a product", description = "Deletes a product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    public ResponseEntity<Object> delete(@RequestParam String sku) {
        return productService.delete(sku);
    }

    @GetMapping()
    @Operation(summary = "Get a product by SKU", description = "Retrieves a single product by its SKU.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> getProductBySku(
            @RequestParam @Parameter(description = "SKU of the product to retrieve", example = "12345") String sku) {
        return productService.get(sku);
    }

    @PutMapping(consumes = "multipart/form-data")
    @Operation(summary = "Update an existing product", description = "Updates an existing product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> updateProduct(
            @RequestParam("skuExisting") String skuExisting,
            @RequestParam("sku") String sku,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("size") String size,
            @RequestParam("weight") String weight,
            @RequestParam("price") Double price,
            @RequestParam("priceUnitary") Double priceUnitary,
            @RequestParam("priceWholesale") Double priceWholesale,
            @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        ProductDTO productDTO = ProductDTO.builder()
                .name(name)
                .sku(sku)
                .category(category)
                .description(description)
                .size(size)
                .weight(weight)
                .price(price)
                .priceUnitary(priceUnitary)
                .priceWholesale(priceWholesale)
                .quantity(quantity)
                .image(image != null && !image.isEmpty() ? image : null)
                .build();

        return productService.update(skuExisting,productDTO);
    }


}
