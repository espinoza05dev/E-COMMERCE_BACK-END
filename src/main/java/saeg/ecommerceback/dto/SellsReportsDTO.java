package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.Period;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellsReportsDTO {
    private Integer id;
    private Period period; // DIARIO, SEMANAL, MENSUAL, ANUAL
    private LocalDate BeginDate;
    private LocalDate EndDate;
    private Integer totalorders;
    private BigDecimal totalsells;
    private BigDecimal averageSell;
    private String TopSell;
    private Integer categoryId;
    private LocalDateTime generationDate;
}
