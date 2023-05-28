package com.project.boardgames.controllers;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.Producer;
import com.project.boardgames.services.GenericServiceImpl;
import com.project.boardgames.utilities.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class ProducerController {
        @Autowired
        private GenericServiceImpl<Producer> producerService;

        @GetMapping
        public ResponseEntity<List<Producer>> getProducers() {
            List<Producer> producers = producerService.returnAllEntitiesById();
            return ResponseEntity.ok(producers);
        }

    @PostMapping("/addProducer")
    public ResponseEntity<RequestResponse<Producer>> addProducer(@Valid @RequestBody Producer newProducer, BindingResult bindingResult) {
        // Validate input data
        CheckValues(bindingResult);
        // Save the new producer
        Producer createdProducer = producerService.save(newProducer);
        RequestResponse<Producer> response = new RequestResponse<>(true, createdProducer, "Producer added successfully");
        return ResponseEntity.ok(response);
    }

    public static void CheckValues(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Retrieve the validation errors
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            // Throw your custom exception with the validation errors
            throw new AppException("Validation errors: " + errorMessages, HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
    }

    @GetMapping("/producer/{id}")
        public ResponseEntity<Producer> getProducerById(@PathVariable("id") Long id) {
            Optional<Producer> producer = producerService.returnEntityById(id);
            return ResponseEntity.notFound().build();
        }

//        @PutMapping("/{id}")
//        public ResponseEntity<Producer> updateProducer(@PathVariable("id") Integer id, @Valid @RequestBody Producer updatedProducer) {
//            Producer existingProducer = producerService.getById(id);
//            if (existingProducer != null) {
//                updatedProducer.setProducerId(id);
//                Producer updated = producerService.save(updatedProducer);
//                return ResponseEntity.ok(updated);
//            }
//            return ResponseEntity.notFound().build();
//        }
//
//        @DeleteMapping("/{id}")
//        public ResponseEntity<Void> deleteProducer(@PathVariable("id") Integer id) {
//            Producer producer = producerService.getById(id);
//            if (producer != null) {
//                producerService.delete(producer);
//                return ResponseEntity.noContent().build();
//            }
//            return ResponseEntity.notFound().build();
//        }
//    }

}
