package com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SchedulingKafkaConsumer {

    @KafkaListener(
        topics = "${spring.kafka.consumer.consultation.topic}",
        groupId = "${spring.kafka.consumer.consultation.groupId}"
    )
    public void consume(String message) {
        System.out.println("===== MESSAGE RECEIVED =====");
        System.out.println(message);
        System.out.println("===== SIMULANDO ENVIO DE EMAIL =====");
    }
}
