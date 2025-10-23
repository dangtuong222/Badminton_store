package vn.iotstar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{id}")
    public String detail(@PathVariable("id") String id, Model model) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/products";
        }
        Product product = opt.get();
        model.addAttribute("product", product);
        return "user/product-detail";
    }
}
