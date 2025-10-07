package com.fiap.itmoura.consultation_consumer.scheduling.domain;

import com.fiap.itmoura.consultation_consumer.scheduling.domain.enums.ConsultationStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTest {

    @Test
    void shouldCreateConsultationWithAllProperties() {
        Consultation consultation = new Consultation();
        UUID id = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime finalDate = startDate.plusHours(1);
        String description = "Test consultation";
        ConsultationStatusEnum status = ConsultationStatusEnum.SCHEDULED;

        consultation.setId(id);
        consultation.setStartDate(startDate);
        consultation.setFinalDate(finalDate);
        consultation.setDescription(description);
        consultation.setStatus(status);

        assertEquals(id, consultation.getId());
        assertEquals(startDate, consultation.getStartDate());
        assertEquals(finalDate, consultation.getFinalDate());
        assertEquals(description, consultation.getDescription());
        assertEquals(status, consultation.getStatus());
    }

    @Test
    void shouldAllowNullDescription() {
        Consultation consultation = new Consultation();
        consultation.setDescription(null);

        assertNull(consultation.getDescription());
    }

    @Test
    void shouldAllowNullStatus() {
        Consultation consultation = new Consultation();
        consultation.setStatus(null);

        assertNull(consultation.getStatus());
    }

    @Test
    void shouldSetAndGetId() {
        Consultation consultation = new Consultation();
        UUID id = UUID.randomUUID();

        consultation.setId(id);

        assertEquals(id, consultation.getId());
    }

    @Test
    void shouldSetAndGetStartDate() {
        Consultation consultation = new Consultation();
        LocalDateTime startDate = LocalDateTime.now();

        consultation.setStartDate(startDate);

        assertEquals(startDate, consultation.getStartDate());
    }

    @Test
    void shouldSetAndGetFinalDate() {
        Consultation consultation = new Consultation();
        LocalDateTime finalDate = LocalDateTime.now().plusHours(1);

        consultation.setFinalDate(finalDate);

        assertEquals(finalDate, consultation.getFinalDate());
    }
}
