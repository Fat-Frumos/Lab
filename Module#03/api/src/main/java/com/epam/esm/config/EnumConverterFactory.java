package com.epam.esm.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * A converter factory that converts a string
 * to enum values in a case-insensitive way
 * <p>
 * This converter factory is intended to be used with Spring's conversion service
 * by creating a new {@link Converter} instance
 * for each enum type that needs to be converted.
 */
@Component
public final class EnumConverterFactory
        implements ConverterFactory<String, Enum<?>> {

    @Override
    public <T extends Enum<?>>
    @NotNull Converter<String, T> getConverter(
            @NotNull Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    /**
     * A converter that converts a string to an enum value, ignoring case.
     *
     * @param <T> the enum type that this converter can convert to
     */
    @AllArgsConstructor
    private static class StringToEnum<T extends Enum<?>>
            implements Converter<String, T> {
        private final Class<T> enumType;

        /**
         * Converts the given string to an enum value, ignoring case.
         *
         * @param source the string to convert
         * @return the corresponding enum value,
         * or {@code null} if the input string is empty
         * @throws RuntimeException if the input string does not match any of the enum constants
         */
        @Override
        public T convert(final String source) {
            return source != null && !source.trim().isEmpty()
                    ? Arrays.stream(enumType.getEnumConstants())
                    .filter(constant -> constant.name().equalsIgnoreCase(source.trim()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            String.format("No enum constant %s.%s",
                                    enumType.getCanonicalName(), source)))
                    : enumType.getEnumConstants()[enumType.getEnumConstants().length - 1];
        }
    }
}
