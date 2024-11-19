package ru.hogwarts.school.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.net.URI;
import java.util.*;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestMvc {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    FacultyService facultyService;

    @MockBean
    FacultyRepository facultyRepository;

    @DisplayName("Проверка на добавление факультета")
    @Test
    public void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");
        String content = objectMapper.writeValueAsString(faculty);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @DisplayName("Проверка на получение факультета по идентификатору")
    @Test
    public void readFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @DisplayName("Проверка на получение всех факультетов")
    @Test
    public void readAllFacultiesTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("red");

        when(facultyRepository.findAll()).thenReturn(List.of(faculty, faculty1));
        when(facultyRepository.count()).thenReturn(1L);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()))
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[1].name").value(faculty1.getName()))
                .andExpect(jsonPath("$[1].color").value(faculty1.getColor()));
    }

    @DisplayName("Проверка на редактирование факультета")
    @Test
    public void updateFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");
        faculty.setId(1L);
        String content = objectMapper.writeValueAsString(faculty);

        when(facultyRepository.existsById(anyLong())).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @DisplayName("Проверка на удаление факультета по идентификатору")
    @Test
    public void deleteFacultyTest() throws Exception {
        when(facultyRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(facultyRepository).deleteById(anyLong());

        mockMvc.perform(delete("/faculty/" + nextInt()))
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка на нахождение факультета по наименованию или цвету")
    @Test
    public void filterFacultyByColorOrNameTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString())).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/filter?color=1&name=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
    }

    @DisplayName("Проверка на нахождение студентов по факультету")
    @Test
    public void getStudentsByFacultyTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");
        Set<Student> students = new HashSet<>();
        students.add(student);
        faculty.setStudents(students);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/get/students/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()));
    }
}
