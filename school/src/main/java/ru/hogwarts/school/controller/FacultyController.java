package ru.hogwarts.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@Valid @RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.createFaculty(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> readFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.readFaculty(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> readAllFaculties() {
        return ResponseEntity.ok(facultyService.readAllFaculties());
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@Valid @RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(faculty));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Faculty>> filterFacultiesByNameOrColor(@RequestParam(required = false) String color,
                                                            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(facultyService.filterFacultiesByColorOrName(color, name));
    }

    @GetMapping("get/students/{id}")
    public ResponseEntity<Collection<Student>> getStudentsByFacultyId(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }
}