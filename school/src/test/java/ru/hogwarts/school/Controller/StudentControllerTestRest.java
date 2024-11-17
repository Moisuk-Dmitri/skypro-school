package ru.hogwarts.school.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

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
        assertThat(this.testRestTemplate.getForObject(getAddress(), String.class))
                .isNotNull();
    }

    private String getAddress() {
        return "http://localhost:" + port + "/student";
    }

}
