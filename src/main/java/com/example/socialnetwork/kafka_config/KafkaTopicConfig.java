package com.example.socialnetwork.kafka_config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic habTopic(){
        return TopicBuilder
                .name("chat-topic")
                .partitions(1)
                .build();
    }
}
