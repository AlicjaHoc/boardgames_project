package com.project.boardgames.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.entities.Order;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.repositories.CartRepository;
import com.project.boardgames.repositories.OrderRepository;
import com.project.boardgames.utilities.RequestResponse;
import com.project.boardgames.utilities.payments.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/v1")
@RestController
public class PaymentController {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;
    @PostMapping("/payment")
    public ResponseEntity<RequestResponse<Order>> processPayment(@Valid @RequestBody JsonNode requestBody, BindingResult bindingResult, HttpServletRequest request) {
        ProducerController.CheckValues(bindingResult);

        String status = requestBody.get("status").asText();
        PaymentResponse paymentResponse;

        try {
            paymentResponse = PaymentResponse.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException("Invalid payment status", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        if (paymentResponse == PaymentResponse.SUCCESS) {
            AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).orElseThrow(() ->
                    new AppException("User not found", HttpStatus.NOT_FOUND.value(), "fail", true));

            Order order = orderRepository.findByUser_IdAndStatus(user.getId(), Order.Status.ACTIVE).orElseThrow(() ->
                    new AppException("Order not found", HttpStatus.NOT_FOUND.value(), "fail", true));

            RequestResponse<Order> response = new RequestResponse<Order>(true, order, "The order was finalized");
            return ResponseEntity.ok(response);
        } else if (paymentResponse == PaymentResponse.ERROR) {
            throw new AppException("An error has occurred with your payment", HttpStatus.BAD_REQUEST.value(), "fail", true);
        } else {
            throw new AppException("Payment was not finalized", HttpStatus.PAYMENT_REQUIRED.value(), "fail", true);
        }
    }

}
