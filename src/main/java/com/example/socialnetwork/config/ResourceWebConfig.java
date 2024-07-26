package com.example.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;


@Configuration
public class ResourceWebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/download/**").addResourceLocations("classpath:/static/").setCacheControl(CacheControl.noCache());

   }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedOrigins("http://localhost:8080");
    }




}
