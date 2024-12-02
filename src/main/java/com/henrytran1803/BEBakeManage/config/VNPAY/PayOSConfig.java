package com.henrytran1803.BEBakeManage.config.VNPAY;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {
    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Bean
    public PayOS payOS() {
        System.out.println("Client ID: " + clientId);
        System.out.println("API Key: " + apiKey);
        System.out.println("Checksum Key: " + checksumKey);
        return new PayOS(clientId, apiKey, checksumKey);
    }
}