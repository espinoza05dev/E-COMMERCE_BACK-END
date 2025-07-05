package saeg.ecommerceback.service;

import com.stripe.model.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import saeg.ecommerceback.repository.IProductRepository;

import java.time.Duration;

@Service
public class ProductService {
    private final IProductRepository productRepository;
    private final CacheService cacheService;

    private static final String PRODUCT_CACHE_KEY = "product:";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public ProductService(IProductRepository productRepository, CacheService cacheService) {
        this.productRepository = productRepository;
        this.cacheService = cacheService;
    }

    public Product getProduct(Integer id) {
        String cacheKey = PRODUCT_CACHE_KEY + id;

        // Intentar obtener del cache
        Product cached = cacheService.getCachedObject(cacheKey, Product.class);
        if (cached != null) {
            return cached;
        }

        // Si no está en cache, obtener de la base de datos
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Guardar en cache
        cacheService.cacheObject(cacheKey, product, CACHE_TTL);

        return product;
    }

    public Product updateProduct(Product product) {
        Product saved = productRepository.save(product);

        // Invalidar cache
        String cacheKey = PRODUCT_CACHE_KEY + product.getId();
        cacheService.delete(cacheKey);

        return saved;
    }
}
