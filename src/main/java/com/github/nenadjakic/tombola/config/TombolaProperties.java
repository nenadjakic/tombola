package com.github.nenadjakic.tombola.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tombola")
@Getter
@Setter
public class TombolaProperties {
    private int from;
    private int to;
    private int pick;
}
