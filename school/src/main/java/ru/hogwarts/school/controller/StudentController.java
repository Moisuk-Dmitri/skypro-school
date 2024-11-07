package ru.hogwarts.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody @Valid Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @GetMapping("{id}")
    public Student readStudent(@PathVariable Long id) {
        return studentService.readStudent(id);
    }

    @GetMapping
    public Collection<Student> readAllStudents() {
        return studentService.readAllStudents();
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student) {
        return ResponseEntity.ok(studentService.updateStudent(student));
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Student>> filterStudentsByAge(@RequestParam @PositiveOrZero int age) {
        return ResponseEntity.ok(studentService.filterStudentsByAge(age));
    }

    @GetMapping("filter/between")
    public ResponseEntity<Collection<Student>> filterStudentsByAgeBetween(@RequestParam @PositiveOrZero int min,
                                                                          @RequestParam @PositiveOrZero int max) {
        return ResponseEntity.ok(studentService.filterByAgeBetween(min, max));
    }

    @GetMapping("get/faculty/{id}")
    public Faculty getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }
}
