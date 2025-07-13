package saeg.ecommerceback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class ProductRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductId;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    private String ProductName;

    @Column(nullable = false)
    @NotNull(message = "Product quantity is required")
    @Positive(message = "Product quantity must be positive")
    private Long ProductQuantity;

    private String ProductDescription;

    private String ProductCurrency;

    @Column(nullable = false)
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private Long ProductPrice;

    @Column(nullable = false)
    @NotNull(message = "Product stock is required")
    @Positive(message = "Product stock must be positive")
    private Long ProductStock;
}
