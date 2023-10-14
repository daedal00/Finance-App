package com.daedal00.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PlaidProperties {
    @Value("${plaid.client.id}")
    public String clientId;

    @Value("${plaid.secret}")
    public String secret;

    @Value("${plaid.environment}")
    public String environment;

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getEnvironmentUrl() {
        switch (environment.toLowerCase()) {
            case "sandbox":
                return "https://sandbox.plaid.com";
            case "development":
                return "https://development.plaid.com";
            case "production":
                return "https://production.plaid.com";
            default:
                throw new IllegalArgumentException("Invalid Plaid environment specified");
        }
    }
}
