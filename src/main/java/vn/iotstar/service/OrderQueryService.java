package vn.iotstar.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.Order;
import vn.iotstar.repository.OrderRepository;

@Service
public class OrderQueryService {

    @Autowired
    private OrderRepository orderRepository;

    public Map<Order.OrderStatus, List<Order>> getOrdersGroupedByStatus(String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Map<Order.OrderStatus, List<Order>> map = new EnumMap<>(Order.OrderStatus.class);
        for (Order o : orders) {
            map.computeIfAbsent(o.getStatus(), k -> new java.util.ArrayList<>()).add(o);
        }
        return map;
    }
}
