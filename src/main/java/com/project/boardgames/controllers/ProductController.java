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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private GenericServiceImpl<Product> genericService;
    @Autowired
    private GenericServiceImpl<Producer> producerService;

    @GetMapping("/products")
    public ResponseEntity<RequestResponse<List<Product>>> getAllProducts(HttpServletRequest request) {
        List<Product> products = genericService.returnAllEntitiesById();
        if (products.isEmpty()) {
            throw new AppException("There are no records", HttpStatus.NO_CONTENT.value(), "fail", true);
        }

        String baseUrl = getBaseUrl(request);

        // Populate the image URLs in each product
        for (Product product : products) {
            String imageUrl = baseUrl + "/images/" + product.getPhoto();
            product.setPhoto(imageUrl);
        }

        RequestResponse<List<Product>> response = new RequestResponse<>(true, products, "The list of products");
        return ResponseEntity.ok(response);
    }

    private String getBaseUrl(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        return baseUrl;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<RequestResponse<Product>> getProductById(@PathVariable("id") Long id, HttpServletRequest request) {
        Optional<Product> product = genericService.returnEntityById(id);
        String baseUrl = getBaseUrl(request);

        if (product.isPresent()) {
            String imageUrl = baseUrl + "/images/" + product.get().getPhoto();
            product.get().setPhoto(imageUrl);
        }

        RequestResponse<Product> response = new RequestResponse<>(true, product.orElse(null), "Product retrieved successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequestResponse<Product>> addProduct(
            @RequestPart("photo") MultipartFile photo,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "producerId", required = false) Long producerId,
            @RequestParam(value = "producerName", required = false) String producerName
            ) throws IOException {
        // Validate input data


        // Handle the photo upload
        try {
            if (!photo.isEmpty()) {
                // Generate a unique filename for the uploaded file
                String filename = StringUtils.cleanPath(photo.getOriginalFilename());
                String filePath = "src/main/resources/static/images/" + filename;

                // Save the file to the upload directory
                Path targetLocation = Paths.get(filePath);
                Files.copy(photo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Create a new Product object and set the fields
                Product newProduct = new Product();
                newProduct.setName(name);
                newProduct.setDescription(description);
                newProduct.setPrice(price);
                newProduct.setPhoto(filename);

                // Set the producer based on the provided producerId or create a new Producer
                if (producerId != null) {
                    Optional<Producer> existingProducerOptional = producerService.returnEntityById(producerId);
                    if (existingProducerOptional.isPresent()) {
                        Producer existingProducer = existingProducerOptional.get();
                        newProduct.setProducer(existingProducer);
                    } else {
                        throw new AppException("Invalid producer ID", HttpStatus.BAD_REQUEST.value(), "fail", true);
                    }
                } else if (producerName != null) {
                    Producer newProducer = new Producer();
                    newProducer.setName(producerName);
                    newProduct.setProducer(producerService.save(newProducer));
                } else {
                    throw new AppException("Producer ID or name is required", HttpStatus.BAD_REQUEST.value(), "fail", true);
                }

                // Save the new product
                Product createdProduct = genericService.save(newProduct);

                RequestResponse<Product> response = new RequestResponse<>(true, createdProduct, "Product added successfully");
                return ResponseEntity.ok(response);
            }
        } catch (IOException error) {
            // Handle the exception and throw a custom AppException
            throw new AppException("Failed to upload image " + error, HttpStatus.INTERNAL_SERVER_ERROR.value(), "fail", true);
        }

        // Throw a custom AppException when the photo is not provided
        throw new AppException("Photo is required", HttpStatus.BAD_REQUEST.value(), "fail", true);
    }




    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<RequestResponse<Void>> deleteProduct(@PathVariable("id") Long id) {
        // Check if the product exists
        Optional<Product> product = genericService.returnEntityById(id);
        if (product.isEmpty()) {
            throw new AppException("Product not found with ID: " + id, HttpStatus.NOT_FOUND.value(), "fail", true);
        }

        // Delete the product
        genericService.deleteEntity(id);

        RequestResponse<Void> response = new RequestResponse<>(true, null, "Product deleted successfully");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/recentProducts")
    public ResponseEntity<RequestResponse<List<Product>>> getRecentProducts() {
        // Retrieve the 5 most recent products
        List<Product> recentProducts = genericService.returnAllEntitiesById();
        recentProducts.sort(Comparator.comparing(Product::getCreatedAt).reversed());
        if (recentProducts.size() > 5) {
            recentProducts = recentProducts.subList(0, 5);
        }

        RequestResponse<List<Product>> response = new RequestResponse<>(true, recentProducts, "The 5 most recent products");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<RequestResponse<Product>> updateProduct(
            @PathVariable("id") Long id,
            @Validated @RequestBody Product updatedProduct,
            BindingResult bindingResult) {
        // Validate input data
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            List<ObjectError> errors = bindingResult.getAllErrors();
            // You can handle the errors according to your requirements
            return ResponseEntity.badRequest().build();
        }

        // Retrieve the existing product
        Optional<Product> existingProduct = genericService.returnEntityById(id);
        if (existingProduct.isEmpty()) {
            throw new AppException("Product not found with ID: " + id, HttpStatus.NOT_FOUND.value(), "fail", true);
        }

        // Set the ID of the updated product to the existing product ID
        updatedProduct.setId(id);

        // Set the createdAt field to the existing product's createdAt value
        updatedProduct.setCreatedAt(existingProduct.get().getCreatedAt());

        // Save the updated product
        Product updatedProductResult = genericService.save(updatedProduct);

        RequestResponse<Product> response = new RequestResponse<>(true, updatedProductResult, "Product updated successfully");
        return ResponseEntity.ok(response);
    }
}


