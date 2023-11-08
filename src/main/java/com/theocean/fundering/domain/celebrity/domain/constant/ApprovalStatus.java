package com.theocean.fundering.domain.celebrity.domain.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum ApprovalStatus {
    PENDING("pending"),
    APPROVED("approved");

    private final String type;

    ApprovalStatus(final String type) {
        this.type = type;
    }
    public static ApprovalStatus fromString(final String type) {
        return Arrays.stream(values())
                .filter(approvalStatus -> approvalStatus.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public String getType() {
        return type;
    }

    public class CelebTypeToStringConverter implements AttributeConverter<ApprovalStatus, String> {

        @Override
        public String convertToDatabaseColumn(final ApprovalStatus attribute) {
            return attribute.getType();
        }

        @Override
        public ApprovalStatus convertToEntityAttribute(final String dbData) {
            return ApprovalStatus.fromString(dbData);
        }
    }
}
