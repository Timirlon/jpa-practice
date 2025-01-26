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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @OneToMany(mappedBy = "category")
    final List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    final List<Option> options = new ArrayList<>();

    public void addProducts(Collection<Product> products) {
        this.products.addAll(products);
    }

    public void addOptions(Collection<Option> options) {
        this.options.addAll(options);
    }
}
