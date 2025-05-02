package com.dzenthai.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record TokenRequest(
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password
) {
}
