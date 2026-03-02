package org.example.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.entity.User;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<User.Status, String> {

    @Override
    public String convertToDatabaseColumn(User.Status status) {
        return status == null ? null : status.name(); // persist as enum name (lowercase)
    }

    @Override
    public User.Status convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return User.Status.valueOf(dbData.trim().toLowerCase()); // handle 'active' or 'ACTIVE' -> lower-case enum
        } catch (IllegalArgumentException e) {
            return null; // unknown value -> null
        }
    }
}
