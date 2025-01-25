package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    User client;

    @ManyToMany
    final List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    String address;

    @JoinColumn(name = "order_date")
    LocalDate orderDate;
}
