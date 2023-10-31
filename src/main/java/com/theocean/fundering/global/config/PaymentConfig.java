package com.theocean.fundering.global.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public IamportClient iamportClient(){
        return new IamportClient("","");
    }
}
