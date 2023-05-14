package com.project.boardgames.services;

import com.project.boardgames.ErrorUtilities.AppException;
import com.project.boardgames.entities.GenericEntity;
import com.project.boardgames.repositories.GenericRepository;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class GenericServiceImpl<T extends GenericEntity> implements GenericService<T> {

    private GenericRepository<T> genericRepository;

    public GenericServiceImpl(GenericRepository<T> genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Override
    public Optional<T> returnEntityById(Long id) {
        Optional<T> entity = genericRepository.findById(id);
        if (entity.isPresent()) return entity;
        else {
            throw new AppException("Object not found with ID: " + id, HttpStatus.NOT_FOUND.value(), "fail", true);
        }
    }

    @Override
    public List<T> returnAllEntitiesById() {
        List<T> allEntities = genericRepository.findAll();
        if (allEntities.isEmpty())
            throw new AppException("There are no results.", HttpStatus.NOT_FOUND.value(), "fail", true);
        return allEntities;
    }

    @Override
    public void deleteEntity(Long id) {
        Optional<T> entity = this.returnEntityById(id);
        GenericEntity gEntity = entity.orElse(null);
        genericRepository.delete(gEntity);
    }

    @Override
    public void deleteAllEntities() {
        List<T> entities = this.returnAllEntitiesById();
        genericRepository.deleteAll(entities);
    }

    @Override
    @Transactional
    public Optional updateEntity(GenericEntity entity) {
        if (entity != null) {
            // Retrieve the existing entity from the database
            Optional<T> existingEntity = this.returnEntityById(entity.getId());

            // Get the class of the entity
            Class<?> entityClass = existingEntity.getClass();

            // Get all methods declared in the entity class
            Method[] methods = entityClass.getMethods();

            // Iterate through the methods and update the corresponding properties of the existing entity
            for (Method method : methods) {
                if (method.getName().startsWith("set")) {
                    try {
                        // Get the property name by removing "set" from the method name
                        String propertyName = method.getName().substring(3);

                        // Find the corresponding getter method in the provided entity
                        Method getterMethod = entityClass.getMethod("get" + propertyName);

                        // Get the value of the property from the provided entity
                        Object value = getterMethod.invoke(entity);

                        // Invoke the setter method on the existing entity to update the property
                        method.invoke(existingEntity, value);
                    } catch (Exception e) {
                        // Handle any exceptions that occur during reflection
                        e.printStackTrace();
                    }
                }
            }

            // The updated entity will be automatically persisted when the transaction is committed

            return existingEntity;
        } else {
            throw new AppException("The object you want to update in database is invalid", HttpStatus.BAD_REQUEST.value(), "fail", true);
        }
    }

    @Override
    public T save(T entity) {
        if (entity != null) {
            return genericRepository.save(entity);
        } else {
            throw new AppException("There is no object to add to the database", HttpStatus.NO_CONTENT.value(), "fail", true);
        }
    }
}
