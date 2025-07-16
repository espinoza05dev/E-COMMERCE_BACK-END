package saeg.ecommerceback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saeg.ecommerceback.dto.ProductRequestDTO;
import saeg.ecommerceback.service.ProductRequestService;

@RestController
@RequestMapping("/api/Controller")
@RequiredArgsConstructor
public class ProductController {
//    private final ProductRequestService productRequestService;

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductRequestDTO> getProduct(@PathVariable Integer id) {
//        return ResponseEntity.ok(productRequestService.getProduct(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<ProductRequestDTO> createProduct(@RequestBody ProductRequestDTO productDTO) {
//        return ResponseEntity.ok(productRequestService.c(productDTO));
//    }
}
