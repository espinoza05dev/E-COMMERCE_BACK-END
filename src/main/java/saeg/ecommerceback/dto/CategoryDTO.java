package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private String categoryImageUrl;
    private Boolean categoryActiva;
    private Integer categoryTotalProductos;
    private Integer categoryLevel;
    private String categoryCategoryParentId;

}
