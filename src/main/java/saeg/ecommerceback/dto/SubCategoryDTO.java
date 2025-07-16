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
public class SubCategoryDTO {
    private Integer id;
    private String name;
    private String decription;
    private String categoryId;
    private String imageurk;
    private Boolean active;
    private Integer order;
    private LocalDateTime CreationDate;
}
