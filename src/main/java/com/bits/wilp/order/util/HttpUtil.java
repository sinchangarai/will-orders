package com.bits.wilp.order.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.bits.wilp.order.model.Product;


@Component
public class HttpUtil {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.base.url}")
    private String userServiceBaseUrl;

    @Value("${product.service.base.url}")
    private String productServiceBaseUrl;

    public boolean isJwtExpired(HttpServletRequest request) {
        String authVal = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authVal);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(userServiceBaseUrl + "/token-expiry",
                    HttpMethod.GET, requestEntity, String.class);
        } catch(RestClientException ioe) {
            System.out.println(ioe.getMessage());
            return true;
        }
    
        if(response.getStatusCode() == HttpStatus.OK)
            return Boolean.parseBoolean(response.getBody());

        return true;

    }

    public Product getProductDetails(String productId) {
        ResponseEntity<Product> response;
        try {
            response = restTemplate.getForEntity(productServiceBaseUrl + "/products/" + productId, Product.class);
        } catch(RestClientException ioe) {
            System.out.println(ioe.getMessage());
            return null;
        }
        return response.getBody();
    }

    public boolean updateProductQuantity(String productId, Integer newQuantity, HttpServletRequest request) {
        ResponseEntity<?> response;
        try {
            String url = productServiceBaseUrl + "/products/" + productId;
            String body = "{\"availableQuantity\": " + newQuantity + "}";
            HttpHeaders headers = new HttpHeaders();
            String authVal = request.getHeader("Authorization");
            headers.setContentType(MediaType.APPLICATION_JSON); 
            headers.set("Authorization", authVal);
            HttpEntity<String> entity = new HttpEntity<String>(body, headers); 

            response = restTemplate.exchange(url, HttpMethod.PUT, entity, Product.class);
            if(response.getStatusCode() == HttpStatus.OK)
                return true;
        } catch(RestClientException ioe) {
            System.out.println(ioe.getMessage());
            return false;
        }
        return false;
    }
}