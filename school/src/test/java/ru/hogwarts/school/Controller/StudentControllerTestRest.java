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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @DisplayName("Проверка на добавление студента")
    @Test
    public void createStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                getAddress(),
                student,
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @DisplayName("Проверка на получение студента по идентификатору")
    @Test
    public void readStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        studentRepository.save(student);

        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                getAddress() + "/" + studentRepository.findAll().getFirst().getId().toString(),
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @DisplayName("Проверка на получение всех студентов")
    @Test
    public void readAllStudentsTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(2);

        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

        assertThat(response.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(response.getBody().get(1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
    }

    @DisplayName("Проверка на редактирование студента")
    @Test
    public void updateStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        studentRepository.save(student);

        student.setAge(2);
        student.setName("Olga");

        RequestEntity<Student> request = new RequestEntity<>(student, HttpMethod.PUT, URI.create(getAddress()));

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @DisplayName("Проверка на удаление студента по идентификатору")
    @Test
    public void deleteStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        studentRepository.save(student);

        ResponseEntity<Student> responseDelete = testRestTemplate.exchange(
                getAddress() + "/" + studentRepository.findAll().getFirst().getId().toString(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }

    @DisplayName("Проверка на нахождение студента по году")
    @Test
    public void filterStudentByAgeTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(5);

        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/filter?age=5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
    }

    @DisplayName("Проверка на нахождение студентов по годам")
    @Test
    public void filterStudentByAgeBetweenTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(5);

        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/filter/between?min=4&max=6",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().size()).isEqualTo(1);
        assertThat(responseFilter.getBody().getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
    }

    @DisplayName("Проверка на нахождение факультета по студенту")
    @Test
    public void getFacultyByStudentTest() {
        Faculty faculty = new Faculty();
        faculty.setColor("green");
        faculty.setName("Hogwarts");

        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);
        student.setFaculty(faculty);

        facultyRepository.save(faculty);
        studentRepository.save(student);

        ResponseEntity<Faculty> responseGetFaculty = testRestTemplate.getForEntity(
                getAddress() + "/get/faculty/" + studentRepository.findAll().getFirst().getId().toString(),
                Faculty.class
        );

        assertThat(responseGetFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseGetFaculty.getBody()).isNotNull();
        assertThat(responseGetFaculty.getBody())
                .isEqualTo(faculty);
    }

    private String getAddress() {
        return "http://localhost:" + port + "/student";
    }

    @AfterEach
    public void resetDb() {
        studentRepository.deleteAll();
    }
}
