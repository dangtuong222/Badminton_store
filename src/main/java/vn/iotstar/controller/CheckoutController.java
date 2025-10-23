package vn.iotstar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.Order;
import vn.iotstar.entity.User;
import vn.iotstar.service.CartService;
import vn.iotstar.service.OrderService;
import vn.iotstar.service.UserService;

@Controller
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkoutPage(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        var uOpt = userService.findByEmail(principal.getName());
        if (uOpt.isEmpty()) return "redirect:/login";
        User user = uOpt.get();
        model.addAttribute("items", cartService.getCart(user.getId()));
        model.addAttribute("address", user.getAddresses());
        return "user/checkout";
    }

    @PostMapping("/checkout")
    public String checkoutCOD(Principal principal,
                              @RequestParam("address") String address,
                              @RequestParam("phone") String phone,
                              Model model) {
        if (principal == null) return "redirect:/login";
        var uOpt = userService.findByEmail(principal.getName());
        if (uOpt.isEmpty()) return "redirect:/login";
        Order order = orderService.checkoutCOD(uOpt.get(), address, phone);
        return "redirect:/orders";
    }
}
