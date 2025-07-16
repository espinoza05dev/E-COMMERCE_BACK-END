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
public class InventoryMovementDTO {
    private Integer id;
    private Integer inventoryId;
    private Integer productId;
    private Integer quantity;
    private String typeMovement; // ENTRADA, SALIDA, AJUSTE
    private String reason; // VENTA, COMPRA, DEVOLUCION, AJUSTE_MANUAL
    private String userId;
    private String referenceId; // ordenId, devolucionId, etc.
    private LocalDateTime Datemovement;
}
