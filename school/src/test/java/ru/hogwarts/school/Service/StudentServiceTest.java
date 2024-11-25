package ru.hogwarts.school.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.AgeMinAboveMaxException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        student1 = new Student("David", 23);
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
        assertThrows(WrongIndexException.class, () -> studentService.readStudent(1L));
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
    @DisplayName("Положительный тест на фильтрацию студента по интервалу возраста")
    public void shouldReturnStudentsWhenFilterByAgeBetween() {
        when(studentRepositoryMock.findByAgeBetween(20,25)).thenReturn(new HashSet<Student>(List.of(student1)));

        assertEquals(new HashSet<Student>(List.of(student1)), studentService.filterByAgeBetween(20,25));

        verify(studentRepositoryMock, times(1)).findByAgeBetween(20,25);
    }

    @Test
    @DisplayName("Отрицательный тест на фильтрацию студента по интервалу возраста при минимальном возрасте больше максимального")
    public void shouldThrowExceptionStudentWhenFilterByAgeBetweenWhenMinAgeAboveMax() {
        assertThrows(AgeMinAboveMaxException.class, () -> studentService.filterByAgeBetween(30,25));
    }

    @Test
    @DisplayName("Положительный тест на получение факультета по идентификатору студента")
    public void shouldReturnFacultyByStudentId() {
        Faculty faculty = new Faculty();
        student1.setFaculty(faculty);

        when(studentRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(student1));

        assertEquals(student1.getFaculty(), studentService.getFacultyByStudentId(1L));

        verify(studentRepositoryMock, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Положительный тест на подсчет всех студентов")
    public void shouldReturnNumberOfStudents() {
        when(studentRepositoryMock.countAllStudents()).thenReturn(1L);

        assertThat(studentService.countAllStudents())
                .isEqualTo(1L);

        verify(studentRepositoryMock, times(1)).countAllStudents();
    }

    @Test
    @DisplayName("Положительный тест на подсчет среднего возраста")
    public void shouldReturnAverageAge() {
        when(studentRepositoryMock.getAverageAge()).thenReturn(12.3);

        assertThat(studentService.getAverageAge())
                .isEqualTo(12.3);

        verify(studentRepositoryMock, times(1)).getAverageAge();
    }

    @Test
    @DisplayName("Положительный тест на получение 5 последних студентов")
    public void shouldReturnFiveLastStudents() {
        when(studentRepositoryMock.findFiveLast()).thenReturn(List.of(student1));

        assertThat(studentService.findFiveLast())
                .isEqualTo(List.of(student1));

        verify(studentRepositoryMock, times(1)).findFiveLast();
    }
}
