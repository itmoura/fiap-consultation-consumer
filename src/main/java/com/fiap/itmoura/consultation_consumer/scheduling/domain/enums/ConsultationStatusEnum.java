package com.fiap.itmoura.consultation_consumer.scheduling.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsultationStatusEnum {
    SCHEDULED("SCHEDULED"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");

    private final String status;
}
