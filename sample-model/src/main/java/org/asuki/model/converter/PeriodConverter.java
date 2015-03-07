package org.asuki.model.converter;

import java.time.Period;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PeriodConverter implements AttributeConverter<Period, String> {

    /**
     * @see Period#toString()
     */
    @Override
    public String convertToDatabaseColumn(Period attribute) {
        return Objects.toString(attribute, null);
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        return Period.parse(dbData);
    }

}
