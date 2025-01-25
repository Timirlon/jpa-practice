package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Category;
import model.Option;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CategoryService {
    private static final Scanner scanner = new Scanner(System.in);

    public static List<Category> findAll(EntityManager entityManager) {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c ORDER BY c.id", Category.class);

        return query.getResultList()
                .stream()
                .peek(category -> System.out.println(category.getId() + ". " + category.getName()))
                .toList();
    }

    public static List<Category> findAllWithExtraInfo(EntityManager entityManager) {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c ORDER BY c.id", Category.class);

        return query.getResultList()
                .stream()
                .peek(category -> {
                    System.out.println(category.getId() + ". " + category.getName());
                    category.getProducts().forEach(prod -> {
                        System.out.println(prod.getName() + " " + prod.getPrice());
                        prod.getValues().forEach(value -> System.out.println("- " + value.getOption().getName() + " " + value.getName()));
                    });
                })
                .toList();
    }

    public static Category findById(EntityManager entityManager) {
        System.out.print("Введите id: ");
        int id = scanner.nextInt();

        Category category = entityManager.find(Category.class, id);
        System.out.println(category);

        return category;
    }

    public static void create(EntityManager manager) {
        System.out.print("Введите название категории: ");
        String name = scanner.nextLine();

        Category category = new Category();
        category.setName(name);


        try {

            manager.getTransaction().begin();
            manager.persist(category);
            manager.getTransaction().commit();

            System.out.println("Категория создана");
            System.out.println(category);

        } catch (Exception e) {

            manager.getTransaction().rollback();
            System.out.println("Категория с таким названием уже существует.");
        }
    }

    public static void createWithOptions(EntityManager manager) {
        System.out.print("Введите название категории: ");
        String name = scanner.nextLine();

        Category category = new Category();
        category.setName(name);

        System.out.println("Введите названия характеристики (через запятую и пробел):");
        String optionNames = scanner.nextLine();
        String[] splitOptionNames = optionNames.split(", ");

        List<Option> options = Arrays.stream(splitOptionNames)
                .map(optionName -> {
                    Option option = new Option();
                    option.setName(optionName);
                    option.setCategory(category);

                    return option;
                })
                .toList();


        try {

            manager.getTransaction().begin();
            manager.persist(category);
            options.forEach(manager::persist);

            manager.getTransaction().commit();

            System.out.println("Категория создана");
            System.out.println(category);

        } catch (Exception e) {

            manager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }

    public static void update(EntityManager manager) {
        System.out.print("Введите идентификатор категории: ");
        int id = scanner.nextInt();

        Category category = manager.find(Category.class, id);

        if (category == null) {
            System.out.println("Категории с id: " + id + " - не существует");
            return;
        }

        System.out.print("Введите новое название категории: ");
        String name = scanner.next();
        category.setName(name);


        try {

            manager.getTransaction().begin();
            manager.merge(category);
            manager.getTransaction().commit();

            System.out.println("Категория обновлена.");

        } catch (Exception e) {

            System.out.println("Категория с таким названием уже существует.");
            manager.getTransaction().rollback();
        }
    }
}
