package vn.iotstar.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.CartItem;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CartItemRepository;
import vn.iotstar.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem addToCart(User user, String productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        Product product = productOpt.get();

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);
        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + Math.max(1, quantity));
        } else {
            item = new CartItem();
            item.setId("CI" + UUID.randomUUID());
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(Math.max(1, quantity));
        }
        BigDecimal unitPrice = product.getPromotionalPrice() != null && product.getPromotionalPrice().compareTo(BigDecimal.ZERO) > 0
                ? product.getPromotionalPrice() : product.getPrice();
        item.setPrice(unitPrice);
        return cartItemRepository.save(item);
    }

    public void removeFromCart(String userId, String cartItemId) {
        Optional<CartItem> itemOpt = cartItemRepository.findById(cartItemId);
        if (itemOpt.isPresent() && itemOpt.get().getUser() != null && userId.equals(itemOpt.get().getUser().getId())) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
