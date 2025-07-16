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
public class NotificationDTO {
        private Integer id;
        private String userId;
        private String Title;
        private String msg;
        private List<Type> type; // ORDEN, PROMOCION, SISTEMA, INVENTARIO
        private Boolean read;
        private String channel; // EMAIL, SMS, PUSH, IN_APP
        private LocalDateTime CreationDate;
        private LocalDateTime Dateread;
}