package org.asuki.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.asuki.common.util.Cryptos;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {

        return Cryptos.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {

        return Cryptos.decrypt(dbData);
    }
}
