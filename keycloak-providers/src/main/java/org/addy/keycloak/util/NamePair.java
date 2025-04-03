package org.addy.keycloak.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record NamePair(
        String first,
        String last
) {
    public static NamePair of(String fullName) {
        String[] words = fullName.split("\\s+");

        return switch (words.length) {
            case 0, 1 -> new NamePair(fullName, "");
            default -> {
                int middle = words.length - words.length / 2;
                yield new NamePair(
                        Stream.of(words).limit(middle).collect(Collectors.joining(" ")),
                        Stream.of(words).skip(middle).collect(Collectors.joining(" "))
                );
            }
        };
    }

    public NamePair withFirst(String first) {
        return new NamePair(first, last);
    }

    public NamePair withLast(String last) {
        return new NamePair(first, last);
    }

    @Override
    public String toString() {
        return String.join(" ", first, last).strip();
    }
}
