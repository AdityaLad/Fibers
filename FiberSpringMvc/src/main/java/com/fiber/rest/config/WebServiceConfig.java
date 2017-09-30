package com.fiber.rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import co.paralleluniverse.springframework.web.servlet.config.annotation.FiberWebMvcConfigurationSupport;

@Configuration

@EnableAsync
@Import(FiberWebMvcConfigurationSupport.class)
@ComponentScan(basePackages = {
"com.fiber.rest.controller, com.fiber.rest.util" })
public class WebServiceConfig {

}

