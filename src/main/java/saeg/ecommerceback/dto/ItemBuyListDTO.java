package saeg.ecommerceback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemBuyListDTO {
    private Integer id;
    private String productoId;
    private String nombreProducto;
    private String sku;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;
    private String imagenUrl;
    private Map<String, String> variantes; // talla, color, etc.
    private Boolean disponible;
    private Integer stockDisponible;
    private String categoriaId;
    private String vendedorId;
    private Boolean esDigital;
    private Map<String, Object> metadatos;
}
