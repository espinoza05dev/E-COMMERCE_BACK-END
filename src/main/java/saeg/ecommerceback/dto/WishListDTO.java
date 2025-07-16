package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListDTO {
    private Integer id;
    private String userId;
    private String name;
    private List<Integer> productsIds;
    private Boolean publicc;
    private Boolean active;
    private LocalDateTime CreationDate;
    private LocalDateTime ModifiedDate;
}
