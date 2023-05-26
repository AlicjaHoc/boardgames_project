package com.project.boardgames.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "products")
@DiscriminatorColumn(name = "entity_type")
@DiscriminatorValue("product")
public class Product extends GenericEntity {

    @NotBlank(message = "Name is required")
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "synopsis")
    private String synopsis;

    public Producer getProducer() {
        return producer;
    }
    @ManyToOne
    @JoinColumn(name = "producer_id")
    @JsonBackReference
    private Producer producer;
    @Column(name = "price", precision = 19, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Transient
    private String producerName;

    @Column(name = "photo")
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getProducerName() {
        if (producer != null) {
            return producer.getName();
        }
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

}

