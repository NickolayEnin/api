package ru.otus.stream_api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RareTerminalOperationsTest extends TestCommon {

    @Test
    @DisplayName("Сведение к одному значению (Reduce) - 1")
    void students_FilterApplied_AreFiltered() {
        List<Student> students = getStudentsStream().toList();

        Optional<Student> hybridStudent = students.stream()
            .reduce((previousStudent, nextStudent) -> Student.builder()
                .id(5)
                .points(previousStudent.getPoints() + nextStudent.getPoints())
                .build());

        assertThat(hybridStudent.orElseThrow().getPoints()).isEqualTo(210);
    }

    @Test
    @DisplayName("Преобразование структуры (To Map & To List & To Set) - 1")
    void students_ConvertedIntoMap_MapIsReturned() {
        Stream<Student> students = getStudentsStream();
        List<Student> studentsList = getStudentsStream().toList();
        Set<Student> studentsSet = getStudentsStream().collect(Collectors.toSet());

        Map<Integer, Integer> studentsIdsToPointsMap = students
            .collect(Collectors.toMap(
                Student::getId,
                Student::getPoints));

        Integer idOneStudentPoints = studentsIdsToPointsMap.entrySet().stream()
            .filter(entry -> entry.getKey().equals(1))
            .map(Entry::getValue)
            .findAny().orElseThrow();

        assertThat(idOneStudentPoints).isZero();
    }

    @Test
    @DisplayName("Объединение по одному полю (Grouping By) - 1")
    void students_GroupedByPoints_GroupsAreReturned() {
        Stream<Student> students = getStudentsStream();

        Map<Integer, List<Student>> studentsByPointsGroups = students
            .collect(Collectors.groupingBy(Student::getPoints));

        assertThat(studentsByPointsGroups).hasSize(3);
    }

    @Test
    @DisplayName("Объединение со вложениями по нескольким полям (Grouping By) - 2")
    void students_GroupedByPointsAndDescriptions_AreFiltered() {
        Stream<Student> students = getStudentsStream();

        // Группировка по нескольким полям:
        Map<Integer, Map<String, List<Student>>> studentsByPointsAndDescriptionsGroups = students.collect(
            Collectors.groupingBy(Student::getPoints,
                Collectors.groupingBy(Student::getDescription)));

        assertThat(studentsByPointsAndDescriptionsGroups).hasSize(3);
        Map<String, List<Student>> hundredPointsGroupDescriptionGroups = studentsByPointsAndDescriptionsGroups.get(100);
        List<Student> noDescriptionGroupStudents = hundredPointsGroupDescriptionGroups.get("Нет");
        assertThat(noDescriptionGroupStudents).hasSize(2);
    }

    @Test
    @DisplayName("Разбивка (Partitioning By) - 1")
    void students_PartitioningBy_GroupsAreReturned() {
        Stream<Student> students = getStudentsStream();

        Map<Boolean, List<Student>> studentsByPointsGroups = students
            .collect(Collectors.partitioningBy(student -> student.getPoints() > 1));

        List<Student> noPointsStudents = studentsByPointsGroups.get(false);
        List<Student> havingPointsStudents = studentsByPointsGroups.get(true);

        assertThat(noPointsStudents).hasSize(1);
        assertThat(havingPointsStudents).hasSize(3);
    }

    @Test
    @DisplayName("Статистика (Averaging, Sum) - 1")
    void students_AveragePointsCalculated_AreReturned() {
        double averagePoints = getStudentsStream()
            .collect(Collectors.averagingInt(Student::getPoints));

        assertThat(averagePoints).isEqualTo(52.5);

        int overallPoints = getStudentsStream().mapToInt(Student::getPoints).sum();

        assertThat(overallPoints).isEqualTo(210);
    }
}