package library.timothy.history;

import java.util.LinkedList;
import java.util.List;


public class OrderRepository {
    public static List<Order> getOrders() {
        List<Order> orders = new LinkedList<Order>();


        Order order = new Order("1");
        order.getProducts().add(new Product("A","Product A"));
        order.getProducts().add(new Product("B","Product B"));
        order.getProducts().add(new Product("C","Product C"));
        orders.add(order);

        Order order2 = new Order("2");
        order2.getProducts().add(new Product("E","Product E"));
        order2.getProducts().add(new Product("F","Product F"));
        order2.getProducts().add(new Product("G","Product G"));
        orders.add(order2);

        return orders;
    }
}
