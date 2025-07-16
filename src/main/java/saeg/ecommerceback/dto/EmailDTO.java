package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.StateOrder;
import saeg.ecommerceback.model.Type;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDTO {
    private Integer emailid;
    private String email;
    private String subject;
    private String sendto;
    private String body;
    private List<Type> type; // CONFIRMACION, PROMOCION, NOTIFICACION
    private List<StateOrder> state; // PENDIENTE, ENVIADO, FALLIDO
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;

}
