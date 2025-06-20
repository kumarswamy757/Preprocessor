package com.ayra.preprocessor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.ayra.preprocessor.api.KafkaProducer;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    @Value("${kafka.topic.name}")
    private String topicName;
    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String message) {
        kafkaProducer.sendMessage(topicName, message);
        return "Message sent to Kafka!";
    }
}