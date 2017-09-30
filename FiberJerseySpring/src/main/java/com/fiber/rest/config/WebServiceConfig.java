package com.fiber.rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration

@EnableAsync
@ComponentScan(basePackages = {
        "com.fiber.rest.controller, com.fiber.rest.util" })
public class WebServiceConfig {

}

