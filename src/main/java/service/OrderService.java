package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class OrderService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void create(EntityManager manager) {

        User user = UserService.authorize(manager);

        if (user == null) {
            return;
        }

        System.out.println("Список товаров");
        ProductService.findAll(manager);
        System.out.println();


        System.out.println("Выберите товары: ");
        String productIds = scanner.nextLine();
        String[] strSplitIds = productIds.split(", ");

        List<Integer> splitIds = Arrays.stream(strSplitIds).map(Integer::parseInt).toList();

        TypedQuery<Product> query = manager.createQuery("SELECT p FROM Product p WHERE p.id = :id", Product.class);



        System.out.println("Введите адрес: ");
        String address = scanner.nextLine();

        Order order = new Order();
        order.setClient(user);
        order.setStatus(OrderStatus.CREATED);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());

        try {
            manager.getTransaction().begin();
            manager.persist(order);

            List<Product> productList = new ArrayList<>();

            for (int i = 0; i < splitIds.size(); i++) {
                query.setParameter("id", splitIds.get(i));

                productList.add(query.getSingleResult());
            }

            Map<Product, Integer> prodQuantity = new HashMap<>();

            productList.forEach(prod -> {

                if (prodQuantity.containsKey(prod)) {
                    prodQuantity.put(prod, prodQuantity.get(prod) + 1);
                    return;
                }

                prodQuantity.put(prod, 1);
            });


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

            List<Double> priceList = productList.stream()
                    .map(Product::getPrice)
                    .toList();

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
