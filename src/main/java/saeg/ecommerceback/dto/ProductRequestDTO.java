package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    private Long ProductId;
    private String ProductName;
    private Long ProductQuantity;
    private String ProductDescription;
    private String ProductCurrency;
    private Long ProductPrice;
    private Long ProductStock;
}
