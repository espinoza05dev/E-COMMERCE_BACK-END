package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.StateOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Integer id;
    private String Ordernum; // Número visible para el cliente
    private String userId;
    private StateOrder state; // PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA
    private List<OrderItemDTO> items;

    // Importes
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal discounts;
    private BigDecimal SendCost;
    private BigDecimal total;
    private String currency;

    // Fechas
    private LocalDateTime Creationdate;
    private LocalDateTime ConfirmationDate;
    private LocalDateTime SendDate;
    private LocalDateTime CancelationDate;
}
