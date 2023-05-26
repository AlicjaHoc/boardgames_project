package com.project.boardgames.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.*;
import com.project.boardgames.repositories.AddressRepository;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.repositories.CartRepository;
import com.project.boardgames.repositories.OrderRepository;
import com.project.boardgames.utilities.RequestResponse;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class OrderController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/getAllOrders")
    public ResponseEntity<RequestResponse<List<Order>>> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        RequestResponse<List<Order>> response = new RequestResponse<List<Order>>(true, orders, "List of orders");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/createOrder")
    public ResponseEntity<RequestResponse<Order>> addOrder(@Valid @RequestBody JsonNode requestBody, BindingResult bindingResult, HttpServletRequest request) {
        ProducerController.CheckValues(bindingResult);
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();
        Address address = user.getAddress();
        if((address == null) || (requestBody.get("set_new_address").asBoolean()==true)) {
            address = new Address();
            address.setCity(sanitizeInput(requestBody.get("city").asText()));
            address.setCountry(sanitizeInput(requestBody.get("country").asText()));
            address.setState(sanitizeInput(requestBody.get("state").asText()));
            address.setZip(sanitizeInput(requestBody.get("zip").asText()));
            address.setStreet(sanitizeInput(requestBody.get("street").asText()));
            user.setAddress(address);
            addressRepository.save(address);
            appUserRepository.save(user);
        }
        Optional<Cart> oCart = cartRepository.findByUser_Id(user.getId());
        if(oCart.isEmpty()) throw new AppException("Cart is missing", HttpStatus.BAD_REQUEST.value(), "fail", true);
        String additionalInformation = requestBody.get("additional_information").asText();
        Optional<Order> ongoingOrder = orderRepository.findByUser_IdAndStatus(user.getId(), Order.Status.ACTIVE);
        if(ongoingOrder.isPresent()) {
            if(ongoingOrder.get().getStatus()== Order.Status.ACTIVE) throw new AppException("There is an other ongoing order", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        Cart cart = oCart.get();
        Order order = new Order();
        order.setAdditionalInfo(additionalInformation);
        order.setCart(cart);
        order.setUser(user);
        order.setTotal(cart.getTotalPrice());
        order.setDeliveryAddress(address);
        order.setStatus(Order.Status.ACTIVE);
        cart.setUser(null);
        cartRepository.save(cart);
        orderRepository.save(order);
    RequestResponse<Order> response = new RequestResponse<Order>(true, order, "The order was successfully created. Awaiting payment.");
    return ResponseEntity.ok(response);
    }

    @GetMapping("/finalizeOrder")
    public ResponseEntity<RequestResponse<Order>> finalizeOrder(HttpServletRequest request){
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();
        Optional<Order> orderO = orderRepository.findByUser_IdAndStatus(user.getId(), Order.Status.ACTIVE);
        if(orderO.isEmpty()) throw new AppException("Something went wrong with finalizing your order", HttpStatus.BAD_REQUEST.value(), "fail", true);
        Order order = orderO.get();
        order.setStatus(Order.Status.INACTIVE);
        orderRepository.save(order);
        RequestResponse<Order> response = new RequestResponse<Order>(true, order, "Your order was successfully finalized.");
        return ResponseEntity.ok(response);
    }
    private String sanitizeInput(String input) {
        // Replace any characters that are not alphanumeric, space, Polish special letters, or forward slash with an empty string
        return input.replaceAll("[^a-zA-Z0-9 \\p{L}/]", "");
    }

}
