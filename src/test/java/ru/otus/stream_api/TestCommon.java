package ru.otus.stream_api;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

abstract class TestCommon {

    @Builder
    protected static class Student {

        @Getter
        private int id;

        @Getter
        @Setter
        private int points;

        @Getter
        private String description;

        @Getter
        private List<String> hobbies;

        @Getter
        private boolean isSenior;

        @Getter
        private ZonedDateTime examDateTime;
    }

    protected static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return isTrue -> seen.putIfAbsent(keyExtractor.apply(isTrue), Boolean.TRUE) == null;
    }

    protected Stream<Student> getStudentsStream() {
        return Stream.of(
            Student.builder()
                .id(1)
                .hobbies(List.of("Книги", "ТВ"))
                .description("Нет")
                .examDateTime(now().plusDays(1))
                .build(),
            Student.builder()
                .id(2)
                .points(100)
                .isSenior(true)
                .description("Нет")
                .examDateTime(now().plusDays(2))
                .build(),
            Student.builder()
                .id(3)
                .points(10)
                .description("Есть")
                .examDateTime(now().plusDays(3))
                .build(),
            Student.builder()
                .id(4)
                .points(100)
                .hobbies(List.of("Лапта"))
                .description("Нет")
                .examDateTime(now().plusDays(4))
                .build()
        );
    }
}
