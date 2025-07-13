package saeg.ecommerceback.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import saeg.ecommerceback.model.ProductRequest;
import saeg.ecommerceback.repository.IProductRequestRepository;

import java.time.Duration;

@Service

public class ProductRequestService {
    private final IProductRequestRepository productRepository;
    private final CacheService cacheService;

    private static final String PRODUCT_CACHE_KEY = "product:";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public ProductRequestService(IProductRequestRepository productRepository, CacheService cacheService) {
        this.productRepository = productRepository;
        this.cacheService = cacheService;
    }

    public ProductRequest getProduct(Integer id) {
        String cacheKey = PRODUCT_CACHE_KEY + id;

        // Intentar obtener del cache
        ProductRequest cached = cacheService.getCachedObject(cacheKey, ProductRequest.class);
        if (cached != null) {
            return cached;
        }

        // Si no está en cache, obtener de la base de datos
        ProductRequest product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Guardar en cache
        cacheService.cacheObject(cacheKey, product, CACHE_TTL);

        return product;
    }

    public ProductRequest updateProduct(ProductRequest product) {
        ProductRequest saved = productRepository.save(product);

        // Invalidar cache
        String cacheKey = PRODUCT_CACHE_KEY + product.getProductId();
        cacheService.delete(cacheKey);

        return saved;
    }

}
