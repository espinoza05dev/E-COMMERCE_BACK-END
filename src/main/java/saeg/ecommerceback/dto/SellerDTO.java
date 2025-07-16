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
public class SellerDTO {
    private Integer id;
    private String name;
    private String email;
    private Integer phone;
    private String company;
    private String address;
    private List<StateOrder> state; // ACTIVO, INACTIVO, SUSPENDIDO
    private BigDecimal comition;
    private LocalDateTime fechaRegistro;
}
