package com.rusticworld.app.service;

import com.rusticworld.app.dto.OrderDTO;
import com.rusticworld.app.model.OrderEntity;
import com.rusticworld.app.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;

    public OrderEntity save(OrderDTO orderDTO) {
        return orderRepository.save(fromDTO(orderDTO));
    }

    public OrderEntity get(String code) {
        return orderRepository.findByCode(code)
                .orElse(null);
    }

    public void delete(String code) {
        orderRepository.findByCode(code)
                .ifPresent(order -> orderRepository.delete(order));
    }

    private OrderEntity fromDTO(OrderDTO orderDTO) {
        return OrderEntity.builder()
                .code(orderDTO.getCode())
                .status("Created")
                .total(null)
                .registerDate(LocalDate.now())
                .paymentDate(null)
                .updateDate(LocalDate.now())
                .build();
    }

    public void pay(String code,String paymentType) {
        OrderEntity order = get(code);
        if (order != null){
            order.setUpdateDate(LocalDate.now());
            order.setPaymentDate(LocalDate.now());
            order.setPaymentType(paymentType);
            order.setStatus("Payed");
        }
    }

    public void cancel(String code) {
        OrderEntity order = get(code);
        if (order != null){
            order.setUpdateDate(LocalDate.now());
            order.setStatus("Canceled");
        }
    }

}
