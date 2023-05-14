package com.project.boardgames.entities;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "app_users")
@DiscriminatorColumn(name = "entity_type")
@DiscriminatorValue("app_user")
public class AppUser extends GenericEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email
    @Column
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "password_updated_at")
    private LocalDateTime passwordUpdatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "valid", columnDefinition = "boolean default true")
    private Boolean valid;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // Constructors, getters, setters, and other methods

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public LocalDateTime getPasswordUpdatedAt() {
        return passwordUpdatedAt;
    }

    public void setPasswordUpdatedAt(LocalDateTime passwordUpdatedAt) {
        this.passwordUpdatedAt = passwordUpdatedAt;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }


}
