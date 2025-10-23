package vn.iotstar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getTop10Products() {
        return productRepository.findTop10ByOrderBySoldDesc();
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrueAndIsSellingTrue();
    }

    // additional helpers
    public List<Product> findByCategory(String categoryId) {
        return productRepository.findByCategoryIdAndIsActiveTrueAndIsSellingTrue(categoryId);
    }

    public Page<Product> searchProducts(String keyword, String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isBlank()) {
            return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrueAndIsSellingTrue(keyword, pageable);
        }
        if (categoryId != null && !categoryId.isBlank()) {
            return productRepository.findByCategoryIdAndIsActiveTrueAndIsSellingTrue(categoryId, pageable);
        }
        return productRepository.findByIsActiveTrueAndIsSellingTrue(pageable);
    }

}