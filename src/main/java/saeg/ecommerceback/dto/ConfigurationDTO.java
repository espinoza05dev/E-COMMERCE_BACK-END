package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationDTO {
    private Integer id;
//    private String clave; esta se pondra en el model y no aqui
    private String valor;
    private String categoria;
    private String descripcion;
    private LocalDateTime fechaModificacion;
}
