package vn.iotstar.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderItem;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CartItemRepository;
import vn.iotstar.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order checkoutCOD(User user, String address, String phone) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setId("O" + UUID.randomUUID());
        order.setUser(user);
        order.setAddress(address);
        order.setPhone(phone);
        order.setStatus(Order.OrderStatus.NOT_PROCESSED);
        order.setIsPaidBefore(false);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            OrderItem oi = new OrderItem();
            oi.setId("OI" + UUID.randomUUID());
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getPrice());
            oi.setTotal(ci.getPrice().multiply(java.math.BigDecimal.valueOf(ci.getQuantity())));
            items.add(oi);
            total = total.add(oi.getTotal());
        }
        order.setOrderItems(items);
        order.setAmountFromUser(total);
        order.setAmountToStore(total);

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByUserId(user.getId());
        return saved;
    }
}
