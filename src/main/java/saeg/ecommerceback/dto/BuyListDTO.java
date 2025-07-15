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
public class BuyListDTO {
    private Long buylistId;
    private String buylistusuarioId;
    private List<ItemBuyListDTO> buylistItems;
    private BigDecimal buylistSubtotal;
    private BigDecimal buylistImpuestos;
    private BigDecimal buylistDescuentos;
    private BigDecimal buylistTotal;
    private String buylistMoneda;
    private LocalDateTime buylistFechaCreacion;
    private LocalDateTime buylistFechaModificacion;
//    private EstadoCarrito estado;
    private String buylistCodigoCupon;
//    private String direccionEntregaId;
//    private String metodoEnvioId;
//    private BigDecimal costoEnvio;
    private LocalDateTime buylistFechaExpiracion;
    private String buylistSessionId;
    private List<String> buylistMensajesValidacion;
}
