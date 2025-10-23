package vn.iotstar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.iotstar.service.OrderQueryService;
import vn.iotstar.service.UserService;

@Controller
public class OrdersController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderQueryService orderQueryService;

    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        var uOpt = userService.findByEmail(principal.getName());
        if (uOpt.isEmpty()) return "redirect:/login";
        model.addAttribute("ordersByStatus", orderQueryService.getOrdersGroupedByStatus(uOpt.get().getId()));
        return "user/orders";
    }
}
