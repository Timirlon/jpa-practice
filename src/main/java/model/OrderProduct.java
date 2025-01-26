package model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders_products")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    Order order;

    @ManyToOne
    Product product;

    int quantity;
}
