package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Category;
import model.Product;
import model.Value;

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

    public static void create(EntityManager manager) {
        CategoryService.findAll(manager);

        Category category = CategoryService.findById(manager);

        System.out.print("Введите название товара: ");
        String productName = scanner.nextLine();

        System.out.print("Введите стоимость товара: ");
        double price = scanner.nextDouble();

        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        product.setCategory(category);


        List<Value> values = category.getOptions()
                .stream()
                .map(option -> {
                    System.out.print(option.getName() + ": ");
                    String valueName = scanner.next();
                    Value value = new Value();

                    value.setName(valueName);
                    value.setProduct(product);
                    value.setOption(option);
                    return value;
                })
                .toList();



        try {

            manager.getTransaction().begin();
            manager.persist(product);
            values.forEach(manager::persist);

            manager.getTransaction().commit();

            System.out.println("Товар создан");

        } catch (Exception e) {

            manager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }
}
