package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void findInRange(EntityManager manager) {
        System.out.println("Введите минимальное значение диапазона: ");
        double min = scanner.nextDouble();

        System.out.println("Введите максимальное значение диапазона: ");
        double max = scanner.nextDouble();


        TypedQuery<Product> query = manager.createQuery("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max", Product.class);
        query.setParameter("min", min);
        query.setParameter("max", max);


        List<Product> products = query.getResultList();
        System.out.println(products);
    }
}
