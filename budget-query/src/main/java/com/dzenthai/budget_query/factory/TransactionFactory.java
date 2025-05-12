package com.dzenthai.budget_query.factory;

import com.dzenthai.budget_query.model.dto.Event;
import com.dzenthai.budget_query.model.dto.Payload;
import com.dzenthai.budget_query.model.enums.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TransactionFactory<T> {

    private final ObjectMapper objectMapper;

    private final Class<T> payloadClass;

    public TransactionFactory(Class<T> payloadClass) {
        this.payloadClass = payloadClass;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    public Payload<T> convertMessageToPayload(String message) {
        try {
            JavaType eventType = objectMapper.getTypeFactory()
                    .constructParametricType(Event.class, payloadClass);

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Event<T> event = objectMapper.readValue(message, eventType);

            if (event != null && event.operation() != null) {
                T beforeData = event.before();
                T afterData = event.after();
                return switch (event.operation()) {
                    case "c" -> new Payload<>(afterData, OperationType.CREATE);
                    case "d" -> new Payload<>(beforeData, OperationType.DELETE);
                    default  -> {
                        log.warn("TransactionFactory | Unknown operation: {}", event.operation());
                        yield null;
                    }
                };
            }
            log.warn("TransactionFactory | Invalid event or operation: {}", event);
            return null;
        } catch (JsonProcessingException e) {
            log.error("TransactionFactory | Failed to process message: {}", message, e);
            return null;
        }
    }
}
