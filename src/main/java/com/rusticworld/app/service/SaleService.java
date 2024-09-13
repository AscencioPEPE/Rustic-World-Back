package com.rusticworld.app.service;

import com.rusticworld.app.model.ClientEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SaleService {
    private ClientService clientService;
    private OrderService orderService;
    private ProductOrderedService productOrderedService;

    @Transactional
    public ResponseEntity<Object> createOrder(String clientName, String orderCode){
        ClientEntity client = clientService.getClient(clientName);
        return ResponseEntity.ok().body(client);
    }
}
