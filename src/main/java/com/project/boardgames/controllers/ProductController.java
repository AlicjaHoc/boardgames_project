package com.project.boardgames.controllers;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.Producer;
import com.project.boardgames.entities.Product;
import com.project.boardgames.services.GenericServiceImpl;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class ProductController {
    @Autowired
    private GenericServiceImpl<Product> genericService;
    @Autowired
    private GenericServiceImpl<Producer> producerService;

    @GetMapping("/products")
    public ResponseEntity getAllProducts() {
        System.out.println(1);
        List<Product> products = genericService.returnAllEntitiesById();
        if (products.isEmpty())
            throw new AppException("There are no records", HttpStatus.NO_CONTENT.value(), "fail", true);
        RequestResponse<List<Product>> response = new RequestResponse<List<Product>>(true, products, "The list of products: ");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequestResponse<Product>> addProduct(
            @Validated @RequestPart("product") Product newProduct,
            @RequestPart("photo") MultipartFile photo,
            BindingResult bindingResult) throws IOException {

        // Validate input data
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            List<ObjectError> errors = bindingResult.getAllErrors();
            // You can handle the errors according to your requirements
            return ResponseEntity.badRequest().build();
        }

        // Handle the photo upload
        try {
            if (!photo.isEmpty()) {
                // Generate a unique filename for the uploaded file
                String filename = StringUtils.cleanPath(photo.getOriginalFilename());
                String filePath = "src/main/resources/static/images/" + filename;

                // Save the file to the upload directory
                Path targetLocation = Paths.get(filePath);
                Files.copy(photo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Set the photo field of the newProduct
                newProduct.setPhoto(filePath);
            }
        } catch (UncheckedIOException error) {
            System.out.println("I hope it won't explode");
        }

        // Save the producer if it's a new entity
        if (newProduct.getProducer().getId() == null) {
            Producer createdProducer = producerService.save(newProduct.getProducer());
            newProduct.setProducer(createdProducer);
        }

        newProduct.setCreatedAt(LocalDateTime.now());
        // Save the new product
        Product createdProduct = genericService.save(newProduct);

        RequestResponse<Product> response = new RequestResponse<>(true, createdProduct, "Product added successfully");
        return ResponseEntity.ok(response);
    }




}


