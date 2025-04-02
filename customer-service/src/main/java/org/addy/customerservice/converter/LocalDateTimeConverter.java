package org.addy.customerservice.converter;

import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.convert.ValueConversionContext;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeConverter implements PropertyValueConverter<LocalDateTime, Long, ValueConversionContext<?>> {

    @Override
    public @NonNull LocalDateTime read(@NonNull Long value, @NonNull ValueConversionContext<?> context) {
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public @NonNull Long write(@NonNull LocalDateTime value, @NonNull ValueConversionContext<?> context) {
        return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
