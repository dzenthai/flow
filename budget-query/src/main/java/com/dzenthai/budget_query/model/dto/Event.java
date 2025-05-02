package com.dzenthai.budget_query.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Event<T>(
        @JsonProperty("after")
        T message,
        @JsonProperty("op")
        String operation
) {
}
