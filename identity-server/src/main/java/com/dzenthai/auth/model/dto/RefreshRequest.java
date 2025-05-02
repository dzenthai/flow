package com.dzenthai.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record RefreshRequest(
        @JsonProperty("token")
        String token
) {
}
