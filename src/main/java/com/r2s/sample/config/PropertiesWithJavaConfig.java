package com.r2s.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Properties with Java config
 * @author kyle
 * @since 2023-08-31
 */
@Configuration
@PropertySource("classpath:messages.properties")
public class PropertiesWithJavaConfig {
}
