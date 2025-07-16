package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DevolutionDTO {
    private Integer ordenId;
    private String usuarioId;
    private List<String> productosIds;
    private String motivo;
    private String estado; // SOLICITADA, APROBADA, RECHAZADA, PROCESADA
    private BigDecimal montoDevolucion;
    private String metodoPago;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaProceso;
}
