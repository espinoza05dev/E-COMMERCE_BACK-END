package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvetoryDTO {
    private Integer id;
    private Integer productId;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer MaximumStock;
    private Integer reservedStock;
    private String ubication;
    private LocalDateTime UpdatedDate;
}
