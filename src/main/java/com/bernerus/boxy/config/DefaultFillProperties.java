package com.bernerus.boxy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "boxy.fill")
@Getter
@Setter
public class DefaultFillProperties {
    private double fillFactor = 0.9; // Default value if not specified in properties
    private int extraAttempts = 10; // Default value if not specified in properties
}
