package com.fiap.itmoura.consultation_consumer.scheduling.application.job;

import com.fiap.itmoura.consultation_consumer.scheduling.application.usecase.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationScheduling {

    private final ConsultationService consultationService;

    @Scheduled(cron = "0 0 23 * * *", zone = "America/Sao_Paulo")
//    @Scheduled(fixedDelay = 1000) caso queira testar a cada segundo
    public void schedule() {
        System.out.println("===== SCHEDULING JOB =====");
        System.out.println("===== LEMBRETE DE CONSULTA =====");
        consultationService.findAllConsultationTomorrow().forEach(System.out::println);
        System.out.println("===== FINALIZANDO JOB =====");
    }
}
