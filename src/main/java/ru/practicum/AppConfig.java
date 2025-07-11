package ru.practicum;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableAutoConfiguration
@ComponentScan("ru.practicum")
@EnableJpaRepositories("ru.practicum")
@EntityScan("ru.practicum")
public class AppConfig {
}
