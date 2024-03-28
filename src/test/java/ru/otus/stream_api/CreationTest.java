package ru.otus.stream_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class CreationTest {

    @Test
    void nothing_Stream_IsCreated() {
        // Генерация конечных потоков:
        Stream<String> arrayAsStream = Stream.of("a", "b", "c");
        assertThat(arrayAsStream).hasSize(3);

        // Генерация по шаблонц "Строитель":
        Stream<Object> streamBuilder = Stream.builder().add("a").add("b").build();
        assertThat(streamBuilder).hasSize(2);

        // Генерация бесконечного потока с указанием лимита:
        Stream<String> generatedStream = Stream.generate(() -> "element").limit(10);
        assertThat(generatedStream).hasSize(10);

        // 20 раз с шагом 2, начиная с 40:
        Stream<Integer> iteratedStream = Stream.iterate(40, n -> n + 2).limit(20);
        assertThat(iteratedStream).hasSize(20);

        // Из списка:
        Stream<String> listOriginStream = List.of("a", "b", "c").stream();
        assertThat(listOriginStream).hasSize(3);

        // Из набора ключ-значение:
        Stream<String> listOriginMap = Map.of(1, "a", 2, "b", 3, "c").values().stream();
        assertThat(listOriginMap).hasSize(3);

        // Генерация диапазона включительно-невключительно:
        IntStream rangeStream = IntStream.range(0, 10);
        assertThat(rangeStream).hasSize(10);
    }
}
