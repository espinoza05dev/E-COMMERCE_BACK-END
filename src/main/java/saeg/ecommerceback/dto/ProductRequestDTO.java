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
    private Long productId;
    private String productName;
    private Long productQuantity;
    private String productDescription;
    private String productCurrency;
    private Long productPrice;
    private Long productStock;
}
