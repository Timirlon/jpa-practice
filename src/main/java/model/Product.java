package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.*;

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
    final Set<Value> values = new HashSet<>();

    @OneToMany(mappedBy = "product")
    final Set<Review> reviews = new HashSet<>();

    @ManyToMany
    final List<Order> orders = new ArrayList<>();

    public void addValues(Collection<Value> values) {
        this.values.addAll(values);
    }
}
