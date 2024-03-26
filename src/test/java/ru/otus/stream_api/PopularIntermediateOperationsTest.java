package ru.otus.stream_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PopularIntermediateOperationsTest extends TestCommon {

    @Test
    @DisplayName("Фильтрация (Filter) - 1")
    void students_FilterApplied_AreFiltered() {
        Stream<Student> students = getStudentsStream();

        List<Student> filteredStudents = students
            .filter(student -> student.getPoints() == 100)
            .filter(Student::isSenior)
            .toList();

        assertThat(filteredStudents).hasSize(1);
        assertThat(filteredStudents.get(0).getPoints()).isEqualTo(100);
        assertTrue(filteredStudents.get(0).isSenior());
    }

    @Test
    @DisplayName("Фильтрация (Filter) - 2")
    void students_FilteredByDistinctPoints_UniquePointsReturned() {
        Stream<Student> students = getStudentsStream();

        List<Student> filteredStudents =
            students
                .filter(distinctByKey(Student::getPoints))
                .toList();

        assertThat(filteredStudents).hasSize(3);
    }

    @Test
    @DisplayName("Предобработка (Map) - 1")
    void students_DescriptionsFilledAndToUpperCase_AreReturned() {
        Stream<Student> students = getStudentsStream();

        List<String> seniorStudentDescriptions =
            students
                .filter(Student::isSenior)
                .map(student -> student.getDescription().toUpperCase())
                .toList();

        assertThat(seniorStudentDescriptions).hasSize(1);
        assertThat(seniorStudentDescriptions.get(0)).isEqualTo("НЕТ");
    }

    @Test
    @DisplayName("Предобработка с объединением (FlatMap) - 1")
    void twoDimensionalLetterArrays_Merged_AreReturnedFlat() {
        String[][] arraysArray = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};

        String[] flatArray = Stream.of(arraysArray)
            .flatMap(Stream::of)
            .toArray(String[]::new);

        assertThat(flatArray).hasSize(6);
        assertThat(flatArray[3]).isEqualTo("d");
    }

    @Test
    @DisplayName("Предобработка с объединением (FlatMap) - 2")
    void students_HobbiesMerged_AreReturnedFlat() {
        Stream<Student> students = getStudentsStream();

        Set<String> studentsHobbies = students
            .flatMap(student -> student.getHobbies() == null
                ? Stream.empty()
                : student.getHobbies().stream())
            .collect(Collectors.toSet());

        assertThat(studentsHobbies).hasSize(3);
    }

    @Test
    @DisplayName("Сортировка (Sorted) - 1")
    void students_SortedByPointsAndThenByIdReversed_AreOrdered() {
        Stream<Student> students = getStudentsStream();
        Comparator<Student> comparator = Comparator.comparing(Student::getPoints);
        comparator = comparator.thenComparing(Student::getId, Comparator.reverseOrder());

        List<Student> sortedStudents = students
            .sorted(comparator)
            .collect(Collectors.toList());

        assertThat(sortedStudents).hasSize(4);
        assertThat(sortedStudents.get(0).getId()).isEqualTo(1);
        assertThat(sortedStudents.get(1).getId()).isEqualTo(3);
        assertThat(sortedStudents.get(2).getId()).isEqualTo(4);
        assertThat(sortedStudents.get(3).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Сортировка (Sorted) - 2")
    void students_SortedManually_AreOrdered() {
        Stream<Student> students = getStudentsStream();

        List<Student> sortedStudents = students.sorted((student, otherStudent) -> {
            if (student.getExamDateTime() == null || otherStudent.getExamDateTime() == null) {
                return 1;
            }
            // Даты:
            boolean isAfter = student.getExamDateTime().isAfter(otherStudent.getExamDateTime());
            boolean isEqual = student.getExamDateTime().isEqual(otherStudent.getExamDateTime());

            // Направление сортировки:
            int ascending = isAfter ? 1 : (isEqual ? 0 : -1);
            int descending = isAfter ? -1 : (isEqual ? 0 : 1);
            return descending;
        }).collect(Collectors.toList());

        assertThat(sortedStudents).hasSize(4);
        assertThat(sortedStudents.get(0).getId()).isEqualTo(4);
        assertThat(sortedStudents.get(1).getId()).isEqualTo(3);
        assertThat(sortedStudents.get(2).getId()).isEqualTo(2);
        assertThat(sortedStudents.get(3).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Пропуск (Skip) - 1")
    void students_First_IsSkipped() {
        Stream<Student> students = getStudentsStream();
        List<Student> nonskippedStudents = students.skip(1L).toList();
        assertThat(nonskippedStudents).hasSize(3);
        assertThat(nonskippedStudents.get(0).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Просмотр (Peak) - 1")
    void students_pointsModified_ResultIsReturned() {
        Stream<Student> students = getStudentsStream();

        students.peek(System.out::println); // Вывода в консоль не будет, но поток уже закрылся.

        int modifiedPointsSum = getStudentsStream()
            .map(student -> student.getPoints() * 3)
            .peek(System.out::println)
            .mapToInt(Integer::intValue).sum();

        assertThat(modifiedPointsSum).isEqualTo(630);
    }
}
