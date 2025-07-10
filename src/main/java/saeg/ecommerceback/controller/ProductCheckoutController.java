package saeg.ecommerceback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saeg.ecommerceback.model.ProductRequest;
import saeg.ecommerceback.dto.StripeResponseDTO;
import saeg.ecommerceback.service.StripeService;

@RestController
@RequestMapping("/trialproduct/v1")
public class ProductCheckoutController {

    @Autowired
    private final StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponseDTO> checkoutProducts(@RequestBody ProductRequest productRequest){
        StripeResponseDTO stripeResponseDTO = stripeService.checkoutProducts(productRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponseDTO);
    }
}
