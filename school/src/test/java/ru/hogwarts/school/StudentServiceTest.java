package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.WrongAgeException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentServiceTest {

    private StudentService studentService;
    Student student1;
    Student student2;

    @BeforeEach
    public void setup() {
        student1 = new Student(0L, "David", 23);
        student2 = new Student(0L, "Martin", 14);
        studentService = new StudentService();
    }

    @Test
    @DisplayName("Положительный тест на добавление студента")
    public void shouldReturnStudentWhenCreate() {
        assertEquals(student1, studentService.createStudent(student1));
    }

    @Test
    @DisplayName("Положительный тест на получение студента")
    public void shouldReturnStudentWhenRead() {
        studentService.createStudent(student1);

        assertEquals(student1, studentService.readStudent(1L));
    }

    @Test
    @DisplayName("Отрицательный тест на получение студента")
    public void shouldThrowExceptionWhenRead() {
        assertThrows(WrongIndexException.class, () -> studentService.readStudent(1L));
    }

    @Test
    @DisplayName("Положительный тест на редактирование студента")
    public void shouldReturnStudentWhenUpdate() {
        studentService.createStudent(student1);

        assertEquals(student1, studentService.updateStudent(student1));
    }

    @Test
    @DisplayName("Отрицательный тест на редактирование студента")
    public void shouldThrowExceptionWhenUpdate() {
        assertThrows(WrongIndexException.class, () -> studentService.updateStudent(student1));
    }

    @Test
    @DisplayName("Положительный тест на удаление студента")
    public void shouldReturnStudentWhenDelete() {
        studentService.createStudent(student1);

        assertEquals(student1, studentService.deleteStudent(1L));
    }

    @Test
    @DisplayName("Отрицательный тест на удаление студента")
    public void shouldThrowExceptionWhenDelete() {
        assertThrows(WrongIndexException.class, () -> studentService.deleteStudent(1L));
    }

    @Test
    @DisplayName("Положительный тест на фильтрацию студента по возрасту")
    public void shouldReturnStudentWhenFilterByAge() {
        studentService.createStudent(student1);
        studentService.createStudent(student2);

        assertEquals(new HashSet<Student>(List.of(student1)), studentService.filterStudentsByAge(23));
    }

    @Test
    @DisplayName("Отрицательный тест на фильтрацию студента по возрасту")
    public void shouldThrowExceptionWhenFilterByAge() {
        assertThrows(WrongAgeException.class, () -> studentService.filterStudentsByAge(0));
    }

}
