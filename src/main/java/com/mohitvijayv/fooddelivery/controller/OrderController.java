package com.mohitvijayv.fooddelivery.controller;

import java.util.List;

import com.mohitvijayv.fooddelivery.dto.OrderDto;
import com.mohitvijayv.fooddelivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("time")
    public ResponseEntity getDeliveryTime(@RequestBody List<OrderDto> orders){
        return ResponseEntity.ok(orderService.getDeliveryTime(orders));
    }
}
