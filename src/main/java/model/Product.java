package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "product")
    final List<Value> values = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    final List<OrderProduct> ordersProducts = new ArrayList<>();

    public void addValues(Collection<Value> values) {
        this.values.addAll(values);
    }
}
