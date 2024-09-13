package com.rusticworld.app.controller;

import com.rusticworld.app.dto.ClientDTO;
import com.rusticworld.app.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the system.")
    @ApiResponse(responseCode = "200", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Product not created successfully")
    public ResponseEntity<Object> save(@RequestBody ClientDTO clientDTO) {
        return clientService.save(clientDTO);
    }

    @PutMapping("/{name}")
    @Operation(summary = "Update a product", description = "Updates an existing product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    public ResponseEntity<Object> update(@PathVariable String name, @RequestBody ClientDTO clientDTO) {
        return clientService.update(name, clientDTO);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete a product", description = "Deletes a product by SKU.")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    public ResponseEntity<Object> delete(@PathVariable String name) {
        return clientService.delete(name);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get a client by Name", description = "Retrieves a single client by its Name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the client")
    @ApiResponse(responseCode = "404", description = "client not found")
    public ResponseEntity<Object> get(
            @PathVariable @Parameter(description = "client name to retrieve it", example = "Jane doe") String name) {
        return clientService.get(name);
    }

    @GetMapping()
    @Operation(summary = "Get a client by Name", description = "Retrieves a single client by its Name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the client")
    @ApiResponse(responseCode = "404", description = "client not found")
    public ResponseEntity<Object> getAll() {
        return clientService.getAll();
    }


}
