package saeg.ecommerceback.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BuyListDTO {
    private String id;
    private String usuarioId;
    private List<ItemBuyListDTO> items;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal descuentos;
    private BigDecimal total;
    private String moneda;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
//    private EstadoCarrito estado;
    private String codigoCupon;
//    private String direccionEntregaId;
//    private String metodoEnvioId;
//    private BigDecimal costoEnvio;
    private LocalDateTime fechaExpiracion;
    private String sessionId;
    private List<String> mensajesValidacion;
}
