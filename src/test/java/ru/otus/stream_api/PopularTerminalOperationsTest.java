package ru.otus.stream_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PopularTerminalOperationsTest extends TestCommon {

    @Test
    @DisplayName("Подсчет количества (Count) - 1")
    void students_Counted_ResultIsReturned() {
        Stream<Student> students = getStudentsStream();

        assertThat(students.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("Перебор (ForEach) - 1")
    void students_EachGivenAPoint_SumIsCalculated() {
        List<Student> students = getStudentsStream().toList();

        students.forEach(student -> student.setPoints(1));
        int sum = students.stream().mapToInt(Student::getPoints).sum();

        assertThat(sum).isEqualTo(4);
    }

    @Test
    @DisplayName("Ограничение выборки (Limit) - 1")
    void students_Limited_SelectionIsReturned() {
        Stream<Student> students = getStudentsStream();

        Stream<Student> limitedStudents = students.limit(2);

        assertThat(limitedStudents).hasSize(2);
    }

    @Test
    @DisplayName("Поиск (Find First & Find Any) - 1")
    void students_Searched_FirstIsReturned() {
        Stream<Student> students = getStudentsStream();

        Optional<Student> firstOptional = students.findFirst();

        assertThat(firstOptional.orElseThrow().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Сравнение (Any Match & All Match & None Match) - 1")
    void students_PointsEvaluated_MatchesAreReturned() {
        boolean allMatch = getStudentsStream()
            .allMatch(student -> student.getPoints() > -1);

        assertTrue(allMatch);

        boolean anyMatch = getStudentsStream()
            .anyMatch(student -> student.getPoints() == 0);

        assertTrue(anyMatch);

        boolean noneMatch = getStudentsStream()
            .noneMatch(student -> student.getPoints() == 200);

        assertTrue(noneMatch);
    }

    @Test
    @DisplayName("Склеивание (Joining) - 1")
    void students_HobbiesJoinedCommaSeparated_ResultIsReturned() {
        Stream<Student> students = getStudentsStream();

        String joinedStudentHobbies = students
            .flatMap(student -> student.getHobbies() == null
                ? Stream.empty()
                : student.getHobbies().stream())
            .collect(Collectors.joining(", "));

        assertThat(joinedStudentHobbies).isEqualTo("Книги, ТВ, Лапта");
    }
}
