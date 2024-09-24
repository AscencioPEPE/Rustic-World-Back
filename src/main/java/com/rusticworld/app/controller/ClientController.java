package com.rusticworld.app.controller;

import com.rusticworld.app.dto.ClientDTO;
import com.rusticworld.app.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client in the system.")
    @ApiResponse(responseCode = "200", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Client not created successfully")
    public ResponseEntity<Object> save(@RequestBody ClientDTO clientDTO) {
        return clientService.save(clientDTO);
    }

    @PutMapping
    @Operation(summary = "Update a client", description = "Updates an existing client by name.")
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    public ResponseEntity<Object> update(@RequestParam("name") String name, @RequestBody ClientDTO clientDTO) {
        return clientService.update(name, clientDTO);
    }

    @DeleteMapping
    @Operation(summary = "Delete a client", description = "Deletes a client by name.")
    @ApiResponse(responseCode = "200", description = "Client deleted successfully")
    public ResponseEntity<Object> delete(@RequestParam("name") String name) {
        return clientService.delete(name);
    }

    @GetMapping("/get")
    @Operation(summary = "Get a client by Name", description = "Retrieves a single client by its Name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the client")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Object> get(
            @RequestParam("name") @Parameter(description = "Client name to retrieve it", example = "Jane Doe") String name) {
        return clientService.get(name);
    }

    @GetMapping("/all")
    @Operation(summary = "List all clients", description = "Retrieves a list of clients, optionally filtered by client types and sorted by name.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clients")
    public ResponseEntity<Object> findAll(
            @RequestParam(required = false) @Parameter(description = "List of client types to filter", example = "premium, regular") List<String> clientTypes,
            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Order of name sorting, 'asc' or 'desc'", example = "asc") String sortOrder,
            @RequestParam @Parameter(description = "Number of results per page", example = "10") Integer limit,
            @RequestParam @Parameter(description = "Page number", example = "1") Integer page,
            @RequestParam(required = false) @Parameter(description = "Name prefix to filter clients whose names start with these letters", example = "Jo") String namePrefix
    ) {
        return clientService.getAll(clientTypes, sortOrder, limit, page,namePrefix);
    }

}
