package com.project.boardgames.repositories;

import com.project.boardgames.entities.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenericRepository<T extends GenericEntity> extends JpaRepository<T, Long> {
    Optional<T> findById(Long id);
    void delete( GenericEntity entity);
    void deleteById(Long id);
}
