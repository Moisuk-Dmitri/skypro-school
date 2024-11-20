package ru.hogwarts.school.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @DisplayName("Проверка на добавление факультета")
    @Test
    public void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(
                getAddress(),
                faculty,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @DisplayName("Проверка на получение факультета по идентификатору")
    @Test
    public void readFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                getAddress() + "/" + facultyRepository.findAll().get(0).getId().toString(),
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @DisplayName("Проверка на получение всех факультетов")
    @Test
    public void readAllFacultiesTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("red");

        facultyRepository.save(faculty);
        facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

        assertThat(response.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(response.getBody().get(1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty1);
    }

    @DisplayName("Проверка на редактирование факультета")
    @Test
    public void updateFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        facultyRepository.save(faculty);

        faculty.setColor("red");
        faculty.setName("Slytherin");

        RequestEntity<Faculty> request = new RequestEntity<>(faculty, HttpMethod.PUT, URI.create(getAddress()));

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @DisplayName("Проверка на удаление факультета по идентификатору")
    @Test
    public void deleteFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> responseDelete = testRestTemplate.exchange(
                getAddress() + "/" + facultyRepository.findAll().get(0).getId().toString(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }

    @DisplayName("Проверка на нахождение факультета по наименованию или цвету")
    @Test
    public void filterFacultyByColorOrNameTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("red");

        facultyRepository.save(faculty);
        facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/filter?color=red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty1);

        ResponseEntity<List<Faculty>> responseFilter1 = testRestTemplate.exchange(
                getAddress() + "/filter?name=Gryffindor",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseFilter1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter1.getBody()).isNotNull();
        assertThat(responseFilter1.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty1);

        ResponseEntity<List<Faculty>> responseFilter2 = testRestTemplate.exchange(
                getAddress() + "/filter?color=green&name=Hogwarts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseFilter2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter2.getBody()).isNotNull();
        assertThat(responseFilter2.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @DisplayName("Проверка на нахождение студентов по факультету")
    @Test
    public void getStudentsByFacultyTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        studentRepository.save(student);

        Faculty faculty = new Faculty();
        faculty.setColor("green");
        faculty.setName("Hogwarts");
        faculty.setStudents(new HashSet<>(studentRepository.findAll()));

        facultyRepository.save(faculty);

        ResponseEntity<List<Student>> responseGetStudents = testRestTemplate.exchange(
                getAddress() + "/get/students/" + facultyRepository.findAll().get(0).getId().toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(responseGetStudents.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseGetStudents.getBody()).isNotNull();
        assertThat(responseGetStudents.getBody().get(0))
                .isEqualTo(student);
    }

    private String getAddress() {
        return "http://localhost:" + port + "/faculty";
    }

    @AfterEach
    public void resetDb() {
        facultyRepository.deleteAll();
    }

}
