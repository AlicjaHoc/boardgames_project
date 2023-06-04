package com.project.boardgames.controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.AppUser;
import com.project.boardgames.entities.Cart;
import com.project.boardgames.entities.CartItem;
import com.project.boardgames.entities.Product;
import com.project.boardgames.repositories.AppUserRepository;
import com.project.boardgames.repositories.CartItemRepository;
import com.project.boardgames.repositories.CartRepository;
import com.project.boardgames.repositories.ProductRepository;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1")
@RestController
public class CartController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @PostMapping("/toCart")
    public ResponseEntity<RequestResponse<Cart>> addItemToCart(@Valid @RequestBody JsonNode requestBody, BindingResult bindingResult, HttpServletRequest request) {
        System.out.println(request.getAttribute("username"));
        JsonNode productIdNode = requestBody.get("productId");
        if (productIdNode == null) {
            throw new AppException("There are no products to add", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        Long productId = productIdNode.asLong();
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException("There is no product under that id", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();

        Product product = productOptional.get();

        JsonNode quantityNode = requestBody.get("quantity");
        if (quantityNode == null) {
            throw new AppException("The quantity is missing", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        int quantityToAdd = quantityNode.asInt();

        Optional<Cart> cartOptional = cartRepository.findByUser_Id(user.getId());
        Cart cart;
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();}
            else {
                // Cart does not exist, create a new one
                cart = new Cart();
                cart.setUser(user);
            }

        // Find the existing cart item for the same product, if it exists
        Optional<CartItem> existingCartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst();

        CartItem cartItem;
        if (existingCartItemOptional.isPresent()) {
            // Existing cart item found, update the quantity
            cartItem = existingCartItemOptional.get();
            int existingQuantity = cartItem.getQuantity();
            int updatedQuantity = existingQuantity + quantityToAdd;
            cartItem.setQuantity(updatedQuantity);
        } else {
            // Create a new cart item for the product
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantityToAdd);
            cart.addCartItem(cartItem);
        }
        cart.recalculateTotalPrice();
        cart = cartRepository.save(cart);
        RequestResponse<Cart> response = new RequestResponse<>(true, cart, "The cart was added/updated");
        return ResponseEntity.ok(response);
    }
    @PostMapping("fromCart")
    public ResponseEntity<RequestResponse<Cart>> removeItemFromCart(@Valid @RequestBody JsonNode requestBody, BindingResult bindingResult, HttpServletRequest request) {
        JsonNode productIdNode = requestBody.get("productId");
        if (productIdNode == null) {
            throw new AppException("There are no products to remove", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        Long productId = productIdNode.asLong();
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new AppException("There is no product under that id", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        Product product = productOptional.get();

        JsonNode quantityNode = requestBody.get("quantity");
        int quantityToRemove = (quantityNode != null) ? quantityNode.asInt() : 1;

        // Retrieve the cart based on the user ID
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();


        Optional<Cart> cartOptional = cartRepository.findByUser_Id(user.getId());
        if (cartOptional.isEmpty()) {
            throw new AppException("The cart does not exist", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        Cart cart = cartOptional.get();

        // Find the existing cart item for the product
        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst();

        if (cartItemOptional.isEmpty()) {
            throw new AppException("The specified product is not in the cart", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }

        CartItem cartItem = cartItemOptional.get();
        int existingQuantity = cartItem.getQuantity();
        if (quantityToRemove >= existingQuantity) {
            // Remove the entire cart item
            cart.removeCartItem(cartItem);
        } else {
            // Update the quantity of the cart item
            int updatedQuantity = existingQuantity - quantityToRemove;
            cartItem.setQuantity(updatedQuantity);
        }

        cart.recalculateTotalPrice();
        cart = cartRepository.save(cart);

        RequestResponse<Cart> response = new RequestResponse<>(true, cart, "The product(s) were removed from the cart");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/removeCart")
    public void removeCart(HttpServletRequest request) {
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();
        System.out.println(user);
        Optional<Cart> cartOptional = cartRepository.findByUser_Id(user.getId());
        if (cartOptional.isPresent()) {
            System.out.println('b');
            Cart cart = cartOptional.get();
            cartRepository.delete(cart);
        } else {
            throw new AppException("Cart not found", HttpStatus.NOT_FOUND.value(), "fail", true);
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<RequestResponse<Cart>> getUserCart(HttpServletRequest request) {
        AppUser user = appUserRepository.findByEmail(String.valueOf(request.getAttribute("username"))).get();
        Optional<Cart> cartOptional = cartRepository.findByUser_Id(user.getId());

        if (cartOptional.isEmpty()) {
            throw new AppException("Cart not found", HttpStatus.NOT_FOUND.value(), "fail", true);
        }

        Cart cart = cartOptional.get();
        RequestResponse<Cart> response = new RequestResponse<>(true, cart, "User cart retrieved successfully");
        return ResponseEntity.ok(response);
    }

}
