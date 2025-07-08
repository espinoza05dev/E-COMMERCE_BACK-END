package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private Integer ProductId;
    private String ProductName;
    private String ProductDescription;
    private String ProductCurrency;
    private Long ProductPrice;
    private Long ProductStock;
}
