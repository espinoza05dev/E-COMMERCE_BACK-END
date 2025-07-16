package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.TypeTax;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxDTO {
    private String id;
    private String name;
    private BigDecimal percentage;
    private List<TypeTax> type; // IVA, ISR, RETENCION
    private String country;
    private String state;
    private Boolean active;
    private LocalDateTime CreationDate;
}
