package com.bernerus.boxy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "boxy")
@Getter
@Setter
public class BoxyProperties {
    private double defaultFillFactor = 0.9; // Default value if not specified in properties
}
