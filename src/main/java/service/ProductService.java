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

    public static void update(EntityManager manager) {
        System.out.print("Введите идентификатор товара: ");
        String strProductId = scanner.nextLine();

        int productId = Integer.parseInt(strProductId);
        Product product = manager.find(Product.class, productId);


        System.out.print("Введите новое название товара [" + product.getName() + "]: ");
        String updName = scanner.nextLine();
        if (!updName.isBlank()) {
            product.setName(updName);
        }

        System.out.print("Введите новую стоимость товара [" + product.getPrice() + "]: ");
        String updPrice = scanner.nextLine();
        if (!updPrice.isBlank()) product.setPrice(Double.parseDouble(updPrice));

        manager.getTransaction().begin(); //open

        product.getCategory().getOptions()
                .forEach(option -> {
                    option.getValues()
                            .stream()
                            .filter(value -> value.getProduct().getId() == productId)
                            .findFirst()
                            .ifPresentOrElse(value -> {
                                System.out.print(option.getName() + " [" + value.getName() + "]: ");

                                String updValueName = scanner.nextLine();

                                if (!updValueName.isBlank()) {
                                    value.setName(updValueName);
                                }

                                value.setProduct(product);
                                value.setOption(option);

                                manager.persist(value);
                            }, () -> {
                                System.out.print(option.getName() + " [null]: ");
                                String updValueName = scanner.nextLine();

                                Value value = new Value();
                                value.setName(updValueName);
                                value.setProduct(product);
                                value.setOption(option);

                                option.addValue(value);
                                manager.persist(value);
                            });
                });


        try {


            manager.persist(product);

            manager.getTransaction().commit();

            System.out.println("Товар обновлен");

        } catch (Exception e) {

            manager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }
}
