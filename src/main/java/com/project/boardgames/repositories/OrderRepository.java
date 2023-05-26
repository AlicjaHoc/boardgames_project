package com.project.boardgames.repositories;
import com.project.boardgames.entities.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends GenericRepository<Order>{
    Optional<Order> findByUser_IdAndStatus(Long userId, Order.Status status);

}

