package com.project.boardgames;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.project.boardgames.entities.Producer;
import com.project.boardgames.entities.Product;
import com.project.boardgames.repositories.ProducerRepository;
import com.project.boardgames.repositories.ProductRepository;

@Component
public class AdderToDatabase implements ApplicationRunner {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProducerRepository producerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Producer p1 = new Producer("PANS Krosno");
        producerRepository.save(p1);
        productRepository.save(new Product("Produkt 1", "Opis produktu 1", p1, BigDecimal.valueOf(12.50), "abc.png"));
        productRepository.save(new Product("Produkt 2", "Opis produktu 2", p1, BigDecimal.valueOf(12.50), "abc.png"));
        productRepository.save(new Product("Produkt 3", "Opis produktu 3", p1, BigDecimal.valueOf(12.50), "abc.png"));
        productRepository.save(new Product("Produkt 4", "Opis produktu 4", p1, BigDecimal.valueOf(12.50), "abc.png"));
        productRepository.save(new Product("Produkt 5", "Opis produktu 5", p1, BigDecimal.valueOf(12.50), "abc.png"));
    }


}
