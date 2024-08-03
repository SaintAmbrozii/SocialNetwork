package com.example.socialnetwork.config;

import com.example.socialnetwork.utils.ZoneDateTimeUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;

@Configuration
public class ZoneDateTimeConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ZoneDateTimeUtil(ZoneId.systemDefault()));
    }
}
