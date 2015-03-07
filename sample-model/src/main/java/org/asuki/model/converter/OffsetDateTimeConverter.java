package org.asuki.model.converter;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OffsetDateTimeConverter implements
        AttributeConverter<OffsetDateTime, String> {

    /**
     * @see OffsetDateTime#toString()
     */
    @Override
    public String convertToDatabaseColumn(OffsetDateTime attribute) {
        return Objects.toString(attribute, null);
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(String dbData) {
        return OffsetDateTime.parse(dbData);
    }

}
