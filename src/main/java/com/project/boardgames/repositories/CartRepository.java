package com.project.boardgames.repositories;

import com.project.boardgames.entities.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends GenericRepository<Cart> {
   
    Optional<Cart> findByUser_Id(@Param("userId") long userId);

}
