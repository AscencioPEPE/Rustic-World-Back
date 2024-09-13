package com.rusticworld.app.controller;

import com.rusticworld.app.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/sell")
public class SaleController {
    private SaleService saleService;

    @PostMapping("/{name}")
    @Operation(summary = "Create a new order for a client", description = "Creates a new order in the system.")
    @ApiResponse(responseCode = "200", description = "order created successfully")
    @ApiResponse(responseCode = "400", description = "order not created successfully")
    public ResponseEntity<Object> createOrder(@PathVariable String name,
                                              @RequestParam @Parameter(description = "code", example = "1") String code) {
        return saleService.createOrder(name, code);
    }
}
