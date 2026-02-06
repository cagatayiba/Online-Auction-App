package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.entity.enums.UserRole;
import com.cengo.muzayedebackendv2.validation.annotation.UniqueEmail;
import com.cengo.muzayedebackendv2.validation.annotation.UniquePhone;
import com.cengo.muzayedebackendv2.validation.annotation.ValidTckn;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ValidTckn
@UniqueEmail
@UniquePhone
public class User extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotBlank
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @Past
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "approved", nullable = false)
    private Boolean approved = false;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @NotNull
    @Column(name = "point", nullable = false)
    private Integer point;

    @Transient
    private Long idNumber;

    public String getFullName() {
        return name + " " + surname;
    }
}
