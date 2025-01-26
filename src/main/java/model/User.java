package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String login;

    String password;

    @JoinColumn(name = "registration_date")
    LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "client")
    final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    final List<Review> reviews = new ArrayList<>();
}
