package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComparisonDTO {
    private Integer id;
    private String usuarioId;
    private List<String> productosIds;
    private LocalDateTime fechaCreacion;
}
