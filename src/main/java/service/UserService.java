package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Scanner;

public class UserService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void register(EntityManager manager) {
        System.out.println("Регистрация");
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = BCrypt.hashpw(scanner.nextLine(), BCrypt.gensalt());

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.NO_NAME);

        try {
            manager.getTransaction().begin();
            manager.persist(user);
            manager.getTransaction().commit();

            System.out.println("Вы успешно зарегистрированы.");
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }

    public static void authorize(EntityManager manager) {
        System.out.println("Авторизация");
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        TypedQuery<User> query = manager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class);

        query.setParameter("login", login);

        try {
            User user = query.getSingleResult();

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new RuntimeException();
            }

            System.out.println("Пользователь: " + user.getLogin());
            System.out.println("Дата регистрации: " + user.getRegistrationDate());
            System.out.println("Роль: " + user.getRole());
            System.out.println("Количество заказов: " + user.getOrders().size());
            System.out.println("Количество отзывов: " + user.getReviews().size());

        } catch (Exception e) {
            System.out.println("Ошибка авторизации. Неправильный логин или пароль.");
        }
    }
}
