package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.text.CollationElementIterator;
import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
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
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("filter-by-age/{age}")
    public Collection<Student> filterStudentsByAge(@PathVariable int age) {
        return studentService.filterStudentsByAge(age);
    }

    @GetMapping("filter-by-age-between")
    public Collection<Student> filterStudentsByAgeBetween(@RequestParam int min,
                                                          @RequestParam int max) {
        return studentService.filterByAgeBetween(min, max);
    }

    @GetMapping("get-faculty-by-student-id/{id}")
    public Faculty getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }
}
