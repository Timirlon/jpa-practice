package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "client_id")
    User client;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    String address;

    @Column(name = "order_date")
    LocalDateTime orderDate;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> orderProducts = new ArrayList<>();
}
