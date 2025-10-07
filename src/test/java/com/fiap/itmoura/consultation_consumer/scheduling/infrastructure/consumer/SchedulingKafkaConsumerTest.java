package com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchedulingKafkaConsumerTest {

    @InjectMocks
    private SchedulingKafkaConsumer schedulingKafkaConsumer;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldConsumeMessageAndPrintToConsole() {
        String testMessage = "Test consultation message";

        schedulingKafkaConsumer.consume(testMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("===== MESSAGE RECEIVED ====="));
        assertTrue(output.contains(testMessage));
        assertTrue(output.contains("===== SIMULANDO ENVIO DE EMAIL ====="));
    }

    @Test
    void shouldConsumeEmptyMessage() {
        String emptyMessage = "";

        schedulingKafkaConsumer.consume(emptyMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("===== MESSAGE RECEIVED ====="));
        assertTrue(output.contains("===== SIMULANDO ENVIO DE EMAIL ====="));
    }

    @Test
    void shouldConsumeNullMessage() {
        schedulingKafkaConsumer.consume(null);

        String output = outputStream.toString();
        assertTrue(output.contains("===== MESSAGE RECEIVED ====="));
        assertTrue(output.contains("null"));
        assertTrue(output.contains("===== SIMULANDO ENVIO DE EMAIL ====="));
    }

    @Test
    void shouldConsumeJsonMessage() {
        String jsonMessage = "{\"id\":\"123\",\"description\":\"Medical consultation\"}";

        schedulingKafkaConsumer.consume(jsonMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("===== MESSAGE RECEIVED ====="));
        assertTrue(output.contains(jsonMessage));
        assertTrue(output.contains("===== SIMULANDO ENVIO DE EMAIL ====="));
    }

    @Test
    void shouldConsumeMessageWithSpecialCharacters() {
        String specialMessage = "Consulta médica às 14:30 - Paciente: João & Maria";

        schedulingKafkaConsumer.consume(specialMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("===== MESSAGE RECEIVED ====="));
        assertTrue(output.contains(specialMessage));
        assertTrue(output.contains("===== SIMULANDO ENVIO DE EMAIL ====="));
    }

    @Test
    void shouldPrintMessagesInCorrectOrder() {
        String testMessage = "Order test message";

        schedulingKafkaConsumer.consume(testMessage);

        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());
        
        boolean foundMessageReceived = false;
        boolean foundActualMessage = false;
        boolean foundEmailSimulation = false;
        
        for (String line : lines) {
            if (line.contains("===== MESSAGE RECEIVED =====")) {
                foundMessageReceived = true;
                assertFalse(foundActualMessage);
                assertFalse(foundEmailSimulation);
            } else if (line.contains(testMessage)) {
                foundActualMessage = true;
                assertTrue(foundMessageReceived);
                assertFalse(foundEmailSimulation);
            } else if (line.contains("===== SIMULANDO ENVIO DE EMAIL =====")) {
                foundEmailSimulation = true;
                assertTrue(foundMessageReceived);
                assertTrue(foundActualMessage);
            }
        }
        
        assertTrue(foundMessageReceived);
        assertTrue(foundActualMessage);
        assertTrue(foundEmailSimulation);
    }

    void tearDown() {
        System.setOut(originalOut);
    }
}
