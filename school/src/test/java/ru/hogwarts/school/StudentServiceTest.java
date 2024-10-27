package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.WrongAgeException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepositoryMock;

    @InjectMocks
    private StudentService studentService;
    Student student1;

    @BeforeEach
    public void setup() {
        student1 = new Student(0L, "David", 23);
    }

    @Test
    @DisplayName("Положительный тест на добавление студента")
    public void shouldReturnStudentWhenCreate() {
        when(studentRepositoryMock.save(student1)).thenReturn(student1);

        assertEquals(student1, studentService.createStudent(student1));

        verify(studentRepositoryMock, times(1)).save(student1);
    }

    @Test
    @DisplayName("Отрицательный тест на получение студента")
    public void shouldThrowExceptionWhenRead() {
        when(studentRepositoryMock.existsById(1L)).thenThrow(WrongIndexException.class);

        assertThrows(WrongIndexException.class, () -> studentService.readStudent(1L));

        verify(studentRepositoryMock, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Положительный тест на редактирование студента")
    public void shouldReturnStudentWhenUpdate() {
        when(studentRepositoryMock.existsById(student1.getId())).thenReturn(true);
        when(studentRepositoryMock.save(student1)).thenReturn(student1);

        assertEquals(student1, studentService.updateStudent(student1));

        verify(studentRepositoryMock, times(1)).existsById(student1.getId());
        verify(studentRepositoryMock, times(1)).save(student1);
    }

    @Test
    @DisplayName("Отрицательный тест на редактирование студента")
    public void shouldThrowExceptionWhenUpdate() {
        when(studentRepositoryMock.existsById(student1.getId())).thenThrow(WrongIndexException.class);

        assertThrows(WrongIndexException.class, () -> studentService.updateStudent(student1));

        verify(studentRepositoryMock, times(1)).existsById(student1.getId());
    }

    @Test
    @DisplayName("Отрицательный тест на удаление студента")
    public void shouldThrowExceptionWhenDelete() {
        when(studentRepositoryMock.existsById(1L)).thenThrow(WrongIndexException.class);

        assertThrows(WrongIndexException.class, () -> studentService.deleteStudent(1L));

        verify(studentRepositoryMock, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Положительный тест на фильтрацию студента по возрасту")
    public void shouldReturnStudentWhenFilterByAge() {
        when(studentRepositoryMock.findByAge(23)).thenReturn(new HashSet<Student>(List.of(student1)));

        assertEquals(new HashSet<Student>(List.of(student1)), studentService.filterStudentsByAge(23));

        verify(studentRepositoryMock, times(1)).findByAge(23);
    }

    @Test
    @DisplayName("Отрицательный тест на фильтрацию студента по возрасту")
    public void shouldThrowExceptionWhenFilterByAge() {
        assertThrows(WrongAgeException.class, () -> studentService.filterStudentsByAge(0));
    }

}
