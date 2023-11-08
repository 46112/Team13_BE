package com.theocean.fundering.domain.member.domain.constant;

import com.theocean.fundering.domain.celebrity.domain.constant.CelebGender;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");
    private final String type;
    UserRole(final String type){
        this.type = type;
    }
    public static UserRole fromString(final String type) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.type.equals(type))
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
    public class UserRoleToStringConverter implements AttributeConverter<UserRole, String> {

        @Override
        public String convertToDatabaseColumn(final UserRole attribute) {
            return attribute.getType();
        }

        @Override
        public UserRole convertToEntityAttribute(final String dbData) {
            return UserRole.fromString(dbData);
        }
    }
}
