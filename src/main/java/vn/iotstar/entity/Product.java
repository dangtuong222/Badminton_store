package vn.iotstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    private String slug;
    
    @Column(columnDefinition = "NVARCHAR(1000)")
    private String description;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal price;
    
    @Column(name = "promotional_price", precision = 15, scale = 2)
    private BigDecimal promotionalPrice;
    
    private Integer quantity = 0;
    
    private Integer sold = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = false;
    
    @Column(name = "is_selling")
    private Boolean isSelling = true;
    
    @Column(name = "list_images", columnDefinition = "NVARCHAR(MAX)")
    private String listImages; // JSON array
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @Column(name = "style_value_ids", columnDefinition = "NVARCHAR(MAX)")
    private String styleValueIds; // JSON array
    
    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItem> cartItems;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Review> reviews;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Favorite> favorites;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductView> productViews;
    
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Promotion> promotions;
}