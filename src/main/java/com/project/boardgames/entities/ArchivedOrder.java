package com.project.boardgames.entities;

import com.project.boardgames.entities.GenericEntity;

import javax.persistence.*;

@Entity
@Table(name = "archived_orders")
public class ArchivedOrder extends GenericEntity {

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Define additional fields and mappings as needed

    // ...
}