package org.asuki.model.converter;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.asuki.model.enums.Mode;

@Converter(autoApply = false)
public class ModeConverter implements AttributeConverter<Mode, String> {

    @Override
    public String convertToDatabaseColumn(Mode mode) {
        switch (mode) {
        case READ_ONLY:
            return "r";
        case READ_WRITE:
            return "rw";
        default:
            throw new IllegalArgumentException("Unknown: " + mode);
        }
    }

    @Override
    public Mode convertToEntityAttribute(String dbData) {
        if (isNullOrEmpty(dbData)) {
            return null;
        }

        switch (dbData) {
        case "r":
            return Mode.READ_ONLY;
        case "rw":
            return Mode.READ_WRITE;
        default:
            throw new IllegalArgumentException("Unknown: " + dbData);
        }
    }

}
