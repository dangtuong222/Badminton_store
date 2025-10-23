package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
@Data
public class Promotion {
    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    
    @Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    private String name;
    
    @Column(columnDefinition = "NVARCHAR(1000)")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType = DiscountType.PERCENTAGE;
    
    @Column(name = "discount_value", precision = 15, scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;
    
    @Column(name = "max_discount", precision = 15, scale = 2)
    private BigDecimal maxDiscount = BigDecimal.ZERO;
    
    @Column(name = "min_order_amount", precision = 15, scale = 2)
    private BigDecimal minOrderAmount = BigDecimal.ZERO;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "applies_to")
    @Enumerated(EnumType.STRING)
    private AppliesTo appliesTo = AppliesTo.ALL_PRODUCTS;
    
    @ManyToMany
    @JoinTable(
        name = "promotion_products",
        joinColumns = @JoinColumn(name = "promotion_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
    }
    
    public enum AppliesTo {
        ALL_PRODUCTS, SPECIFIC_PRODUCTS, CATEGORY
    }
}
