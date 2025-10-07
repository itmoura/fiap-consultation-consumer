package com.fiap.itmoura.consultation_consumer.scheduling.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fiap.itmoura.consultation_consumer.scheduling.domain.enums.ConsultationStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
public class Consultation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDate;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime finalDate;

    @Column
    @Enumerated(EnumType.STRING)
    private ConsultationStatusEnum status;

    @Column
    private String description;

}