package saeg.ecommerceback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saeg.ecommerceback.model.ProductRequest;

@Repository
public interface IProductRequestRepository extends JpaRepository<ProductRequest, Integer> {
}
