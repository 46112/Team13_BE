package com.theocean.fundering.domain.celebrity.domain.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum CelebGender {
    MALE("male"),
    FEMALE("female");

    private final String type;

    CelebGender(final String type) {
        this.type = type;
    }
    public static CelebGender fromString(final String type) {
        return Arrays.stream(values())
                .filter(celebGender -> celebGender.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public String getType() {
        return type;
    }

    public class CelebTypeToStringConverter implements AttributeConverter<CelebGender, String> {

        @Override
        public String convertToDatabaseColumn(final CelebGender attribute) {
            return attribute.getType();
        }

        @Override
        public CelebGender convertToEntityAttribute(final String dbData) {
            return CelebGender.fromString(dbData);
        }
    }
}
