package com.project.boardgames.repositories;

import com.project.boardgames.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends GenericRepository<AppUser> {
    AppUser save(AppUser user);
    Optional<AppUser> findByEmail(String email);

}