package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.EmptyColorException;
import ru.hogwarts.school.exception.WrongAgeException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FacultyServiceTest {

    private FacultyService facultyService;
    Faculty faculty1;
    Faculty faculty2;

    @BeforeEach
    public void setup() {
        faculty1 = new Faculty(0L, "1", "red");
        faculty2 = new Faculty(0L, "2", "green");
        facultyService = new FacultyService();
    }

    @Test
    @DisplayName("Положительный тест на добавление студента")
    public void shouldReturnFacultyWhenCreate() {
        assertEquals(faculty1, facultyService.createFaculty(faculty1));
    }

    @Test
    @DisplayName("Положительный тест на получение студента")
    public void shouldReturnFacultyWhenRead() {
        facultyService.createFaculty(faculty1);

        assertEquals(faculty1, facultyService.readFaculty(1L));
    }

    @Test
    @DisplayName("Отрицательный тест на получение студента")
    public void shouldThrowExceptionWhenRead() {
        assertThrows(WrongIndexException.class, () -> facultyService.readFaculty(1L));
    }

    @Test
    @DisplayName("Положительный тест на редактирование студента")
    public void shouldReturnFacultyWhenUpdate() {
        facultyService.createFaculty(faculty1);

        assertEquals(faculty1, facultyService.updateFaculty(faculty1));
    }

    @Test
    @DisplayName("Отрицательный тест на редактирование студента")
    public void shouldThrowExceptionWhenUpdate() {
        assertThrows(WrongIndexException.class, () -> facultyService.updateFaculty(faculty1));
    }

    @Test
    @DisplayName("Положительный тест на удаление студента")
    public void shouldReturnFacultyWhenDelete() {
        facultyService.createFaculty(faculty1);

        assertEquals(faculty1, facultyService.deleteFaculty(1L));
    }

    @Test
    @DisplayName("Отрицательный тест на удаление студента")
    public void shouldThrowExceptionWhenDelete() {
        assertThrows(WrongIndexException.class, () -> facultyService.deleteFaculty(1L));
    }

    @Test
    @DisplayName("Положительный тест на фильтрацию студента по возрасту")
    public void shouldReturnFacultyWhenFilterByAge() {
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);

        assertEquals(new HashSet<Faculty>(List.of(faculty1)), facultyService.filterFacultiesByColor("red"));
    }

    @Test
    @DisplayName("Отрицательный тест на фильтрацию студента по возрасту")
    public void shouldThrowExceptionWhenFilterByAge() {
        assertThrows(EmptyColorException.class, () -> facultyService.filterFacultiesByColor(""));
    }
}
