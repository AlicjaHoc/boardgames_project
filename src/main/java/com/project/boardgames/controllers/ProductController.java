package com.project.boardgames.controllers;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.Product;
import com.project.boardgames.services.GenericServiceImpl;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class ProductController {
    @Autowired
    private GenericServiceImpl<Product> genericService;

    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        System.out.println(1);
        List<Product> products = genericService.returnAllEntitiesById();
        if(products.isEmpty()) throw new AppException("There are no records", HttpStatus.NO_CONTENT.value(), "fail", true);
        RequestResponse<List<Product>> response = new RequestResponse<List<Product>>(true, products, "The list of products: ");
        return ResponseEntity.ok(response);
    }
}
