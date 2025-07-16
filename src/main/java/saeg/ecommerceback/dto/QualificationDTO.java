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
public class QualificationDTO {
    private String id;
    private String userId;
    private String productId;
    private Integer punctuation; // 1-5
    private String coment;
    private Boolean verified;
    private Integer likes;
    private LocalDateTime CreationDate;
}
