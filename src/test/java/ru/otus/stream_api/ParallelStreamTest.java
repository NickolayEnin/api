package ru.otus.stream_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ParallelStreamTest {

    @Test
    void givenList_whenCallingParallelStream_shouldBeParallelStream() {
        List<Long> aList = new ArrayList<>();
        Stream<Long> parallelStream = aList.parallelStream();
        assertTrue(parallelStream.isParallel());
    }

    @Test
    void longs_SummedInParallel_shouldBeEqualToExpectedTotal()
        throws InterruptedException, ExecutionException {

        long first = 1;
        long last = 1_000_000;
        long expectedTotal = (last + first) * last / 2;

        List<Long> aList = LongStream.rangeClosed(first, last)
            .boxed()
            .collect(Collectors.toList());

        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        try {
            long actualTotal = customThreadPool.submit(
                () -> aList.parallelStream().reduce(0L, Long::sum)).get();

            assertEquals(expectedTotal, actualTotal);
        } finally {
            customThreadPool.shutdown();
        }
    }
}
