package org.asuki.model.converter;

import java.nio.ByteBuffer;
import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class UuidToBytesConverter implements AttributeConverter<UUID, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(UUID attribute) {
        if (attribute == null) {
            return null;
        }

        return ByteBuffer.allocate(16)
                .putLong(attribute.getMostSignificantBits())
                .putLong(attribute.getLeastSignificantBits()).array();
    }

    @Override
    public UUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(dbData);
        long most = buffer.getLong();
        long least = buffer.getLong();
        return new UUID(most, least);
    }
}
