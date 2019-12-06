package com.forumservice.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "usr", uniqueConstraints={@UniqueConstraint(columnNames = {"username"})})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullUser.class)
    private Long id;

    @NotNull
    @Column(unique = true)
    @JsonView(Views.ShortUser.class)
    private String username;

    @NotNull
    @JsonView(Views.ShortUser.class)
    private String firstName;

    @NotNull
    @JsonView(Views.ShortUser.class)
    private String lastName;

    @NotNull
    @JsonView(Views.FullUser.class)
    private String password;

    @JsonView(Views.FullUser.class)
    private boolean active;

    @NotNull
    @JsonView(Views.ShortUser.class)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}

