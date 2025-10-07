package com.fiap.itmoura.consultation_consumer.scheduling.application.usecase;

import com.fiap.itmoura.consultation_consumer.scheduling.domain.Consultation;
import com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.persistence.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository repository;

    public List<Consultation> findAllConsultationTomorrow() {
        return repository.findAllConsultationTomorrow(LocalDate.now().plusDays(1).atStartOfDay(), LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
    }

}
