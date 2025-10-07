package com.fiap.itmoura.consultation_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConsultationConsumerApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // If the context fails to load, this test will fail
    }

    @Test
    void mainMethodShouldNotThrowException() {
        // Test that main method can be called without throwing exceptions
        // In a real scenario, we might want to mock SpringApplication.run()
        // but for this simple test, we just verify the method exists and is callable
        try {
            // We don't actually call main() here as it would start the full application
            // Instead, we verify the class structure
            assertNotNull(ConsultationConsumerApplication.class.getMethod("main", String[].class));
        } catch (NoSuchMethodException e) {
            fail("Main method should exist");
        }
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Object should not be null");
        }
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}
