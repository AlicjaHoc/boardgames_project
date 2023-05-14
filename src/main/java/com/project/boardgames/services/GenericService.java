package com.project.boardgames.services;

import com.project.boardgames.entities.GenericEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GenericService<T extends GenericEntity> {
    Optional<T> returnEntityById(Long id);
    List<T> returnAllEntitiesById();
    void deleteEntity(Long id);
    void deleteAllEntities();
    T save(T entity);
    Optional<T> updateEntity(T entity);
}
