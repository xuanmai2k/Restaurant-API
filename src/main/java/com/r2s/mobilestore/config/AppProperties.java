package com.r2s.mobilestore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * This component binds values from application.properties to object via @ConfigurationProperties
 *
 * @author kyle
 * @since 2023-09-07
 */
@Component
@PropertySource("classpath:messages.properties")
@ConfigurationProperties(prefix = "app")
public class AppProperties {
}
