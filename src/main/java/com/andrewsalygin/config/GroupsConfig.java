package com.andrewsalygin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:groups.properties")
public class GroupsConfig {

    @Value("${groups}")
    private String groups;

    @Bean(name = "groups")
    public List<String> groups() {
        return Arrays.asList(groups.split(","));
    }
}
