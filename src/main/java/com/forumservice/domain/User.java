package com.forumservice.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames = {"username"})})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullUser.class)
    private Long id;

    @NotNull
    @Column(unique = true)
    @JsonView(Views.UserWithoutPassword.class)
    private String username;

    @NotNull
    @JsonView(Views.UserWithoutPassword.class)
    private String firstName;

    @NotNull
    @JsonView(Views.UserWithoutPassword.class)
    private String lastName;

    @NotNull
    @JsonView(Views.FullUser.class)
    private String password;
}

