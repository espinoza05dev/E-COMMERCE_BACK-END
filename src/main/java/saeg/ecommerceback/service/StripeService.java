package saeg.ecommerceback.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import saeg.ecommerceback.model.ProductRequest;
import saeg.ecommerceback.dto.StripeResponseDTO;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public StripeResponseDTO checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getProductName())
                        .setDescription(productRequest.getProductDescription()) // Agregar descripción
                        .build();

        SessionCreateParams.LineItem.PriceData priceData
                = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(productRequest.getProductCurrency() == null ? "USD" : productRequest.getProductCurrency())
                .setUnitAmount(productRequest.getProductQuantity()) // Precio unitario
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem
                = SessionCreateParams.LineItem.builder()
                .setQuantity(productRequest.getProductStock()) // Cantidad a comprar
                .setPriceData(priceData) // SOLO un setPriceData
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://localhost:8080/success")
                .setCancelUrl("https://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();


        try {
            Session session = Session.create(params);

            return StripeResponseDTO.builder()
                    .status("SUCCES")
                    .message("Payment session created")
                    .stripe_id(session.getId())
                    .stripe_url(session.getUrl())
                    .build();
        }catch (StripeException e){
            System.out.println(STR."There was an issue: \{e.getMessage()}");

            return StripeResponseDTO.builder()
                    .status("ERROR")
                    .message(STR."Failed to create payment session: \{e.getMessage()}")
                    .stripe_id(null)
                    .stripe_url(null)
                    .build();
        }
    }
}
