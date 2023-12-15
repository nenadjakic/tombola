package com.github.nenadjakic.tombola.config;

import com.github.nenadjakic.tombola.util.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class TombolaConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }
}
