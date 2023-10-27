package com.bits.wilp.order.util;

import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bits.wilp.order.model.Product;


public class HttpUtil {

    private RestTemplate restTemplate = new RestTemplate();

    public boolean isJwtExpired(HttpServletRequest request) {
        String authVal = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authVal);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try (FileReader reader = new FileReader("src/main/resources/application.properties")) {
            // create properties object 
            Properties p = new Properties(); 
            p.load(reader);

            response = restTemplate.exchange(p.getProperty("user.service.base.url") + "/token-expiry",
                    HttpMethod.GET, requestEntity, String.class);
        } catch(RestClientException | IOException ioe) {
            return true;
        }
    
        if(response.getStatusCode() == HttpStatus.OK)
            return Boolean.parseBoolean(response.getBody());

        return true;

    }

    public Product getProductDetails(String productId) {
        ResponseEntity<Product> response;
        try (FileReader reader = new FileReader("src/main/resources/application.properties")) {
            // create properties object 
            Properties p = new Properties(); 
            p.load(reader);
            response = restTemplate.getForEntity(
                p.getProperty("product.service.base.url") + "/products/" + productId, Product.class);
        } catch(RestClientException | IOException ioe) {
            return null;
        }
        return response.getBody();
    }

    public boolean updateProductQuantity(String productId, Integer newQuantity, HttpServletRequest request) {
        ResponseEntity<?> response;
        try (FileReader reader = new FileReader("src/main/resources/application.properties")) {
            // create properties object 
            Properties p = new Properties(); 
            p.load(reader);

            String url = p.getProperty("product.service.base.url") + "/products/" + productId;
            String body = "{\"availableQuantity\": " + newQuantity + "}";
            HttpHeaders headers = new HttpHeaders();
            String authVal = request.getHeader("Authorization");
            headers.setContentType(MediaType.APPLICATION_JSON); 
            headers.set("Authorization", authVal);
            HttpEntity<String> entity = new HttpEntity<String>(body, headers); 

            response = restTemplate.exchange(url, HttpMethod.PUT, entity, Product.class);
            if(response.getStatusCode() == HttpStatus.OK)
                return true;
        } catch(RestClientException | IOException ioe) {
            return false;
        }
        return false;
    }
}