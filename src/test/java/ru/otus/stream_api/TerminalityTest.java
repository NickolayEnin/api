package ru.otus.stream_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class TerminalityTest {

    @Test
    void stream_Terminated_ExceptionIsThrown() {
        Stream<String> stringStream = Stream.of("A", "B", "C", "D");
        long streamSize = stringStream.count();
        assertThat(streamSize).isEqualTo(4);
        assertThrows(IllegalStateException.class, stringStream::count);
    }

    @Test
    void stream_TerminatedAndRecreated_IsReused() {
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("A", "B", "C", "D");
        long firstStreamSize = streamSupplier.get().count();
        long secondStreamSize = streamSupplier.get().count();
        assertThat(firstStreamSize).isEqualTo(4);
        assertThat(secondStreamSize).isEqualTo(4);
    }
}
