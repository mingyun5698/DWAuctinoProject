package com.example.DWShopProject.service;

import com.example.DWShopProject.entity.Order;
import com.example.DWShopProject.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class PayPalService {

    @Value("${paypal.api.base-url}")
    private String paypalBaseUrl;

    private final RestClient restClient;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    public PayPalService(RestClient restClient) {
        this.restClient = restClient;
//        this.clientId = System.getenv("AZsBqy1jVOVH3JHP_jep4bQ1heFKGzHCKmxvkNh7yNTKtWXLI_TEwR3Vrhn4c1_iFhxe6V2DP9s37tFY");
//        this.secret = System.getenv("EN61_Urc3YEO5AmQTZg2AWwNVgYp5nkPJm9sXCLStgNLzzBuZszG8buGITzJJh6_OCKeX_2I_POzbq5f");
    }

    public String getAccessToken() {
        String clientId = "AZsBqy1jVOVH3JHP_jep4bQ1heFKGzHCKmxvkNh7yNTKtWXLI_TEwR3Vrhn4c1_iFhxe6V2DP9s37tFY"; // PayPal에서 제공한 Client ID
        String secret = "EN61_Urc3YEO5AmQTZg2AWwNVgYp5nkPJm9sXCLStgNLzzBuZszG8buGITzJJh6_OCKeX_2I_POzbq5f"; // PayPal에서 제공한 Secret
        String auth = clientId + ":" + secret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        String body = "grant_type=client_credentials";

        Map response = restClient.post()
                .uri("https://api-m.sandbox.paypal.com/v1/oauth2/token")
                .header("Authorization", "Basic " + encodedAuth) // 공백 확인
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .body(Map.class);

        return (String) response.get("access_token");
    }

    public Map<String, Object> createPayment(String accessToken, int totalPrice, String returnUrl) {

        String cancelUrl = "http://localhost:8080/api/paypal/cancel";  // 리디렉션할 URL 설정

        //OrderId의 총 가격 가져오기.
        String paymentJson = String.format(
                "{\"intent\":\"sale\",\"payer\":{\"payment_method\":\"paypal\"},\"transactions\":[{\"amount\":{\"total\":\"%d\",\"currency\":\"USD\"},\"description\":\"Order Payment\"}],\"redirect_urls\":{\"return_url\":\"%s\",\"cancel_url\":\"%s\"}}",
                totalPrice, returnUrl, cancelUrl
        );

        try {
            Map<String, Object> response = restClient.post()
                    .uri(paypalBaseUrl + "/v1/payments/payment")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentJson)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            log.info("Payment response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error creating payment", e);
            return null;
        }
    }

    public Map<String, Object> capturePayment(String accessToken, String paymentId, String payerId) {
        String captureUrl = String.format("%s/v1/payments/payment/%s/execute", paypalBaseUrl, paymentId);
        String captureJson = String.format("{\"payer_id\":\"%s\"}", payerId);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(captureUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(captureJson)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            log.info("Capture response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error capturing payment", e);
            return null;
        }
    }

    }
