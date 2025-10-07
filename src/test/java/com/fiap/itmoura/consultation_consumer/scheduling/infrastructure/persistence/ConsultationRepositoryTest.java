package com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.persistence;

import com.fiap.itmoura.consultation_consumer.scheduling.domain.Consultation;
import com.fiap.itmoura.consultation_consumer.scheduling.domain.enums.ConsultationStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConsultationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConsultationRepository consultationRepository;

    private Consultation consultationTomorrow1;
    private Consultation consultationTomorrow2;
    private Consultation consultationToday;
    private Consultation consultationNextWeek;

    @BeforeEach
    void setUp() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = LocalDate.now().plusDays(7);

        consultationTomorrow1 = new Consultation();
        consultationTomorrow1.setStartDate(tomorrow.atTime(10, 0));
        consultationTomorrow1.setFinalDate(tomorrow.atTime(11, 0));
        consultationTomorrow1.setDescription("Tomorrow consultation 1");
        consultationTomorrow1.setStatus(ConsultationStatusEnum.SCHEDULED);

        consultationTomorrow2 = new Consultation();
        consultationTomorrow2.setStartDate(tomorrow.atTime(14, 0));
        consultationTomorrow2.setFinalDate(tomorrow.atTime(15, 0));
        consultationTomorrow2.setDescription("Tomorrow consultation 2");
        consultationTomorrow2.setStatus(ConsultationStatusEnum.CONFIRMED);

        consultationToday = new Consultation();
        consultationToday.setStartDate(today.atTime(16, 0));
        consultationToday.setFinalDate(today.atTime(17, 0));
        consultationToday.setDescription("Today consultation");
        consultationToday.setStatus(ConsultationStatusEnum.SCHEDULED);

        consultationNextWeek = new Consultation();
        consultationNextWeek.setStartDate(nextWeek.atTime(9, 0));
        consultationNextWeek.setFinalDate(nextWeek.atTime(10, 0));
        consultationNextWeek.setDescription("Next week consultation");
        consultationNextWeek.setStatus(ConsultationStatusEnum.SCHEDULED);

        entityManager.persistAndFlush(consultationTomorrow1);
        entityManager.persistAndFlush(consultationTomorrow2);
        entityManager.persistAndFlush(consultationToday);
        entityManager.persistAndFlush(consultationNextWeek);
    }

    @Test
    void shouldFindConsultationsTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atStartOfDay();
        LocalDateTime endDate = tomorrow.atTime(LocalTime.MAX);

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(consultationTomorrow1));
        assertTrue(result.contains(consultationTomorrow2));
        assertFalse(result.contains(consultationToday));
        assertFalse(result.contains(consultationNextWeek));
    }

    @Test
    void shouldReturnEmptyListWhenNoConsultationsTomorrow() {
        entityManager.remove(consultationTomorrow1);
        entityManager.remove(consultationTomorrow2);
        entityManager.flush();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atStartOfDay();
        LocalDateTime endDate = tomorrow.atTime(LocalTime.MAX);

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindConsultationsInSpecificTimeRange() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atTime(13, 0); // After first consultation
        LocalDateTime endDate = tomorrow.atTime(16, 0);   // After second consultation

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(consultationTomorrow2));
        assertFalse(result.contains(consultationTomorrow1));
    }

    @Test
    void shouldFindConsultationsAtExactBoundaries() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atTime(10, 0); // Exact start time of first consultation
        LocalDateTime endDate = tomorrow.atTime(14, 0);   // Exact start time of second consultation

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(consultationTomorrow1));
        assertTrue(result.contains(consultationTomorrow2));
    }

    @Test
    void shouldReturnEmptyListForFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        LocalDateTime startDate = futureDate.atStartOfDay();
        LocalDateTime endDate = futureDate.atTime(LocalTime.MAX);

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindConsultationsRegardlessOfStatus() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atStartOfDay();
        LocalDateTime endDate = tomorrow.atTime(LocalTime.MAX);

        List<Consultation> result = consultationRepository.findAllConsultationTomorrow(startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify different statuses are included
        boolean hasScheduled = result.stream().anyMatch(c -> c.getStatus() == ConsultationStatusEnum.SCHEDULED);
        boolean hasConfirmed = result.stream().anyMatch(c -> c.getStatus() == ConsultationStatusEnum.CONFIRMED);
        
        assertTrue(hasScheduled);
        assertTrue(hasConfirmed);
    }
}
