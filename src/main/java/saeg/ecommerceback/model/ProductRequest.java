package saeg.ecommerceback.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ProductId;
    String ProductName;
    String ProductDescription;
    String ProductCurrency;
    Long ProductPrice;
    Long ProductStock;
}
