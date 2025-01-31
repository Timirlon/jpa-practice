package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class OrderService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void create(EntityManager manager) {

        // 1 - авторизация (логин и пароль)
        User user = UserService.authorize(manager);

        if (user == null) {
            return;
        }

        // 2 - вывод списка товаров
        System.out.println("Список товаров");
        ProductService.findAll(manager);
        System.out.println();


        // 3 - товары
        System.out.println("Выберите товары: ");
        String strProductIds = scanner.nextLine();
        String[] strSplitIds = strProductIds.split(", ");

        List<Integer> productIds = Arrays.stream(strSplitIds).map(Integer::parseInt).toList();

        TypedQuery<Product> query = manager.createQuery("SELECT p FROM Product p WHERE p.id = :id", Product.class);

        Map<Product, Integer> prodQuantity = new HashMap<>();
        List<Double> priceList = new ArrayList<>();

        try {
            productIds.forEach(id -> {
                query.setParameter("id", id);

                Product prod = query.getSingleResult();

                if (prodQuantity.containsKey(prod)) {
                    prodQuantity.put(prod, prodQuantity.get(prod) + 1);

                } else {
                    prodQuantity.put(prod, 1);
                }

                priceList.add(prod.getPrice());
            });

        } catch (Exception e) {
            System.out.println("Ошибка с товарами.");
            System.out.println(e.getMessage());
        }


        // 4 - адрес
        System.out.println("Введите адрес: ");
        String address = scanner.nextLine();

        Order order = new Order();
        order.setClient(user);
        order.setStatus(OrderStatus.CREATED);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());


        // 5 - persist
        try {
            manager.getTransaction().begin();
            manager.persist(order);


            for (Map.Entry<Product, Integer> entry : prodQuantity.entrySet()) {
                Product prod = entry.getKey();
                int quantity = entry.getValue();

                OrderProduct op = new OrderProduct();
                op.setOrder(order);
                op.setProduct(prod);
                op.setQuantity(quantity);

                manager.persist(op);
            }

            manager.getTransaction().commit();

            System.out.println("Заказ создан.");
            System.out.print("К оплате: ");


            double sum = 0;
            for (int i = 0; i < priceList.size(); i++) {
                sum += priceList.get(i);
                if (i == 0) {
                    System.out.print(priceList.get(i));
                    continue;
                }

                System.out.print(" + " + priceList.get(i));
            }

            System.out.print(" = " + sum);

        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Ошибки при создании заказа.");
            System.out.println(e.getMessage());
        }
    }
}
