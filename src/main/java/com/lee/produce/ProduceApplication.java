package com.lee.produce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProduceApplication {
    /**
     * 交换器
     */
    public static final String EXCHANGE_LEE_TEST = "exchange.lee.test";
    /**
     * routingkey
     */
    public static final String KEY_LEE_TEST = "lee.test.key";

    public static void main(String[] args) {
        SpringApplication.run(ProduceApplication.class, args);
    }
}
