package com.fiap.itmoura.consultation_consumer.scheduling.application.job;

import com.fiap.itmoura.consultation_consumer.scheduling.application.usecase.ConsultationService;
import com.fiap.itmoura.consultation_consumer.scheduling.domain.Consultation;
import com.fiap.itmoura.consultation_consumer.scheduling.domain.enums.ConsultationStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationSchedulingTest {

    @Mock
    private ConsultationService consultationService;

    @InjectMocks
    private ConsultationScheduling consultationScheduling;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Consultation consultation1;
    private Consultation consultation2;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

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
    void shouldExecuteScheduleJobSuccessfully() {
        List<Consultation> consultations = Arrays.asList(consultation1, consultation2);
        when(consultationService.findAllConsultationTomorrow()).thenReturn(consultations);

        consultationScheduling.schedule();

        verify(consultationService).findAllConsultationTomorrow();
        
        String output = outputStream.toString();
        assertTrue(output.contains("===== SCHEDULING JOB ====="));
        assertTrue(output.contains("===== LEMBRETE DE CONSULTA ====="));
        assertTrue(output.contains("===== FINALIZANDO JOB ====="));
    }

    @Test
    void shouldPrintConsultationsWhenFound() {
        List<Consultation> consultations = Arrays.asList(consultation1, consultation2);
        when(consultationService.findAllConsultationTomorrow()).thenReturn(consultations);

        consultationScheduling.schedule();

        String output = outputStream.toString();
        assertTrue(output.contains(consultation1.toString()));
        assertTrue(output.contains(consultation2.toString()));
    }

    @Test
    void shouldExecuteJobWhenNoConsultationsFound() {
        when(consultationService.findAllConsultationTomorrow()).thenReturn(Arrays.asList());

        consultationScheduling.schedule();

        verify(consultationService).findAllConsultationTomorrow();
        
        String output = outputStream.toString();
        assertTrue(output.contains("===== SCHEDULING JOB ====="));
        assertTrue(output.contains("===== LEMBRETE DE CONSULTA ====="));
        assertTrue(output.contains("===== FINALIZANDO JOB ====="));
    }

    @Test
    void shouldCallConsultationServiceOnlyOnce() {
        List<Consultation> consultations = Arrays.asList(consultation1);
        when(consultationService.findAllConsultationTomorrow()).thenReturn(consultations);

        consultationScheduling.schedule();

        verify(consultationService, times(1)).findAllConsultationTomorrow();
    }

    @Test
    void shouldPrintJobMessagesInCorrectOrder() {
        when(consultationService.findAllConsultationTomorrow()).thenReturn(Arrays.asList());

        consultationScheduling.schedule();

        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());
        
        boolean foundSchedulingJob = false;
        boolean foundLembrete = false;
        boolean foundFinalizando = false;
        
        for (String line : lines) {
            if (line.contains("===== SCHEDULING JOB =====")) {
                foundSchedulingJob = true;
                assertFalse(foundLembrete);
                assertFalse(foundFinalizando);
            } else if (line.contains("===== LEMBRETE DE CONSULTA =====")) {
                foundLembrete = true;
                assertTrue(foundSchedulingJob);
                assertFalse(foundFinalizando);
            } else if (line.contains("===== FINALIZANDO JOB =====")) {
                foundFinalizando = true;
                assertTrue(foundSchedulingJob);
                assertTrue(foundLembrete);
            }
        }
        
        assertTrue(foundSchedulingJob);
        assertTrue(foundLembrete);
        assertTrue(foundFinalizando);
    }

    @Test
    void shouldHandleServiceException() {
        when(consultationService.findAllConsultationTomorrow()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> consultationScheduling.schedule());
        
        verify(consultationService).findAllConsultationTomorrow();
    }

    void tearDown() {
        System.setOut(originalOut);
    }
}
