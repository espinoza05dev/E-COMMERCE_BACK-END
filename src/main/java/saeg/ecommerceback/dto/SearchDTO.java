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
public class SearchDTO {
    private Integer id;
    private String userId;
    private String Keyword;
    private String category;
    private Integer results;
    private String filters;
    private String orderBy;
    private LocalDateTime SearchDate;
}
