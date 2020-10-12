package com.example.demo.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ApplicationConfiguration {
  @Bean
  public Gson getGson() {
    return log.isDebugEnabled() ? new GsonBuilder().setPrettyPrinting().create() : new Gson();
  }
}
