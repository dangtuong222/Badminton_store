package vn.iotstar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.entity.User;
import vn.iotstar.service.CartService;
import vn.iotstar.service.UserService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        var userOpt = userService.findByEmail(username);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User user = userOpt.get();
        model.addAttribute("items", cartService.getCart(user.getId()));
        return "user/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(Principal principal, @RequestParam("productId") String productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity) {
        if (principal == null) {
            return "redirect:/login";
        }
        var userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        cartService.addToCart(userOpt.get(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(Principal principal, @RequestParam("id") String cartItemId) {
        if (principal == null) {
            return "redirect:/login";
        }
        var userOpt = userService.findByEmail(principal.getName());
        if (userOpt.isPresent()) {
            cartService.removeFromCart(userOpt.get().getId(), cartItemId);
        }
        return "redirect:/cart";
    }
}
