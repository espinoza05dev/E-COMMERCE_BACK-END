package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {
    private Integer id;
    private String codigo;
    private String tipo; // PORCENTAJE, MONTO_FIJO
    private BigDecimal valor;
    private BigDecimal montoMinimo;
    private Integer usos;
    private Integer usosMaximos;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaVencimiento;
    private Boolean activo;
}
