package ru.hogwarts.school.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTestMvc {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    StudentService studentService;

    @MockBean
    StudentRepository studentRepository;

    @DisplayName("Проверка на добавление студента")
    @Test
    public void createStudentTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);
        String content = objectMapper.writeValueAsString(student);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @DisplayName("Проверка на получение студента по идентификатору")
    @Test
    public void readStudentTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @DisplayName("Проверка на получение всех студентов")
    @Test
    public void readAllStudentsTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(2);

        when(studentRepository.findAll()).thenReturn(List.of(student, student1));
        when(studentRepository.count()).thenReturn(2L);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()))
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[1].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].age").value(student1.getAge()));
    }

    @DisplayName("Проверка на редактирование студента")
    @Test
    public void updateStudentTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);
        student.setId(1L);
        String content = objectMapper.writeValueAsString(student);

        when(studentRepository.existsById(anyLong())).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @DisplayName("Проверка на удаление студента по идентификатору")
    @Test
    public void deleteStudentTest() throws Exception {
        when(studentRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(anyLong());

        mockMvc.perform(delete("/student/" + nextInt()))
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка на нахождение студента по году")
    @Test
    public void filterStudentByAgeTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(5);

        when(studentRepository.findByAge(anyInt())).thenReturn(List.of(student1));

        mockMvc.perform(get("/student/filter?age=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));
    }

    @DisplayName("Проверка на нахождение студентов по годам")
    @Test
    public void filterStudentByAgeBetweenTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(5);

        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(List.of(student1));

        mockMvc.perform(get("/student/filter/between?min=1&max=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));
    }

    @DisplayName("Проверка на нахождение факультета по студенту")
    @Test
    public void getFacultyByStudentTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);
        student.setFaculty(faculty);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/get/faculty/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @DisplayName("Проверка на подсчет студентов")
    @Test
    public void countAllStudents() throws Exception {
        when(studentRepository.countAllStudents()).thenReturn(1L);

        mockMvc.perform(get("/student/count-students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(1L));
    }

    @DisplayName("Проверка на подсчет среднего возраста")
    @Test
    public void getAverageAge() throws Exception {
        when(studentRepository.getAverageAge()).thenReturn(12.3);

        mockMvc.perform(get("/student/get-average-age"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(12.3));
    }

    @DisplayName("Проверка на получение имен начинающихся на А")
    @Test
    public void findStudentsWhoStartWithA() throws Exception {
        Student student1 = new Student("Vladimir", 123);
        Student student2 = new Student("Arseniy", 321);

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student/find-start-with-A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0]").value(student2.getName().toUpperCase()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @DisplayName("Проверка на подсчет среднего возраста V2")
    @Test
    public void getAverageAgeV2() throws Exception {
        Student student1 = new Student("123", 123);
        Student student2 = new Student("321", 321);

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student/get-average-age-v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value((double) (student1.getAge() + student2.getAge()) / 2));
    }
}
