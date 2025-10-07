package com.fiap.itmoura.consultation_consumer.scheduling.infrastructure.persistence;

import com.fiap.itmoura.consultation_consumer.scheduling.domain.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    @Query("SELECT c FROM Consultation c WHERE c.startDate BETWEEN :startDate AND :endDate")
    List<Consultation> findAllConsultationTomorrow(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
