package com.dzenthai.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;


@Builder
public record ExceptionData(
        @JsonProperty("timestamp")
        Instant timestamp,
        @JsonProperty("status_code")
        Integer status,
        @JsonProperty("message")
        String message
) {
}
