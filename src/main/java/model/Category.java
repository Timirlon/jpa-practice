package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    final Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "category")
    final Set<Option> options = new HashSet<>();

    public void addProducts(Collection<Product> products) {
        this.products.addAll(products);
    }

    public void addOptions(Collection<Option> options) {
        this.options.addAll(options);
    }
}
