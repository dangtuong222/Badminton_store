package vn.iotstar.repository;

import vn.iotstar.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIsActiveTrueAndIsSellingTrue();
    List<Product> findByStoreIdAndIsActiveTrue(String storeId);
    List<Product> findByCategoryIdAndIsActiveTrueAndIsSellingTrue(String categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.isSelling = true ORDER BY p.sold DESC")
    List<Product> findTop10ByOrderBySoldDesc();
    
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrueAndIsSellingTrue(String name);

    Page<Product> findByIsActiveTrueAndIsSellingTrue(Pageable pageable);
    Page<Product> findByCategoryIdAndIsActiveTrueAndIsSellingTrue(String categoryId, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrueAndIsSellingTrue(String name, Pageable pageable);
}