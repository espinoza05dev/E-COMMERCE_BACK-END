package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saeg.ecommerceback.model.Category;
import saeg.ecommerceback.model.Priority;
import saeg.ecommerceback.model.SupportState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportDTO {
    private Integer id;
    private String userId;
    private String agentId;
    private String subject;
    private String description;
    private List<Category> category; // TECNICO, FACTURACION, PRODUCTO, GENERAL
    private List<Priority> priority; // BAJA, MEDIA, ALTA, URGENTE
    private List<SupportState> estate; // ABIERTO, EN_PROCESO, RESUELTO, CERRADO
    private LocalDateTime CreationDate;
    private LocalDateTime AnswerDate;
}
