package saeg.ecommerceback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    Long ProductId;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    String ProductName;

    @Column(nullable = false)
    @NotBlank(message = "Product quantity is required")
    Long ProductQuantity;

    String ProductDescription;

    String ProductCurrency;

    @Column(nullable = false)
    @NotBlank(message = "Product price is required")
    Long ProductPrice;

    @Column(nullable = false)
    @NotBlank(message = "Product stock is required")
    Long ProductStock;
}
