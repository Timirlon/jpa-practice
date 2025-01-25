package model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    User reviewer;

    @ManyToOne
    Product product;

    boolean approved;

    int rate;

    String text;

    @JoinColumn(name = "publication_date")
    LocalDate publicationDate;
}
