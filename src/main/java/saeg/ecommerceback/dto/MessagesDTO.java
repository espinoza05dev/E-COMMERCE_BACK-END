package saeg.ecommerceback.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.Type;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagesDTO {
    private Integer frompersonId;
    private Integer topersonId;
    private String contenido;
    private List<Type> type; // SOPORTE, NOTIFICACION, PROMOCION
    private Boolean read;
    private LocalDateTime Datesend;
    private LocalDateTime Dateread;
}
