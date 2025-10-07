package com.fiap.itmoura.consultation_consumer.scheduling.application.usecase;

import com.fiap.itmoura.consultation_consumer.scheduling.domain.Consultation;
import com.fiap.itmoura.consultation_consumer.scheduling.domain.enums.ConsultationStatusEnum;
import com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.persistence.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository repository;

    @InjectMocks
    private ConsultationService consultationService;

    private Consultation consultation1;
    private Consultation consultation2;

    @BeforeEach
    void setUp() {
        consultation1 = new Consultation();
        consultation1.setId(UUID.randomUUID());
        consultation1.setStartDate(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        consultation1.setFinalDate(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
        consultation1.setDescription("Consultation 1");
        consultation1.setStatus(ConsultationStatusEnum.SCHEDULED);

        consultation2 = new Consultation();
        consultation2.setId(UUID.randomUUID());
        consultation2.setStartDate(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
        consultation2.setFinalDate(LocalDateTime.now().plusDays(1).withHour(15).withMinute(0));
        consultation2.setDescription("Consultation 2");
        consultation2.setStatus(ConsultationStatusEnum.CONFIRMED);
    }

    @Test
    void shouldFindAllConsultationTomorrow() {
        List<Consultation> expectedConsultations = Arrays.asList(consultation1, consultation2);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atTime(LocalTime.MAX);

        when(repository.findAllConsultationTomorrow(startOfDay, endOfDay))
                .thenReturn(expectedConsultations);

        List<Consultation> result = consultationService.findAllConsultationTomorrow();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedConsultations, result);
        verify(repository).findAllConsultationTomorrow(startOfDay, endOfDay);
    }

    @Test
    void shouldReturnEmptyListWhenNoConsultationsTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atTime(LocalTime.MAX);

        when(repository.findAllConsultationTomorrow(startOfDay, endOfDay))
                .thenReturn(Arrays.asList());

        List<Consultation> result = consultationService.findAllConsultationTomorrow();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAllConsultationTomorrow(startOfDay, endOfDay);
    }

    @Test
    void shouldCallRepositoryWithCorrectDateRange() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime expectedStartDate = tomorrow.atStartOfDay();
        LocalDateTime expectedEndDate = tomorrow.atTime(LocalTime.MAX);

        when(repository.findAllConsultationTomorrow(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList());

        consultationService.findAllConsultationTomorrow();

        verify(repository).findAllConsultationTomorrow(expectedStartDate, expectedEndDate);
    }

    @Test
    void shouldReturnConsultationsInSameOrderAsRepository() {
        List<Consultation> expectedOrder = Arrays.asList(consultation2, consultation1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        when(repository.findAllConsultationTomorrow(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedOrder);

        List<Consultation> result = consultationService.findAllConsultationTomorrow();

        assertEquals(expectedOrder, result);
        assertEquals(consultation2, result.get(0));
        assertEquals(consultation1, result.get(1));
    }
}
