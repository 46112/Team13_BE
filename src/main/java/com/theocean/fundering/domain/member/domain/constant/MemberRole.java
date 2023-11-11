package com.theocean.fundering.domain.member.domain.constant;

import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;


public enum MemberRole {
    USER("USER"),
    ADMIN("ADMIN");
    private final String type;

    MemberRole(final String type) {
        this.type = type;
    }

    public static MemberRole fromString(final String type) {
        return Arrays.stream(values())
                .filter(memberRole -> memberRole.type.equals(type))
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

    public class MemberRoleToStringConverter implements AttributeConverter<MemberRole, String> {

        @Override
        public String convertToDatabaseColumn(final MemberRole attribute) {
            return attribute.getType();
        }

        @Override
        public MemberRole convertToEntityAttribute(final String dbData) {
            return fromString(dbData);
        }
    }
}
