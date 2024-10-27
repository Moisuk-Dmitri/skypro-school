package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EmptyColorException;
import ru.hogwarts.school.exception.EmptyDatabaseException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new WrongIndexException();
        }

        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> readAllFaculties() {
        if (facultyRepository.count() == 0) {
            throw new EmptyDatabaseException();
        }

        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            throw new WrongIndexException();
        }

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new WrongIndexException();
        }

        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> filterFacultiesByColor(String color) {
        if (color.isEmpty()) {
            throw new EmptyColorException();
        }

        return facultyRepository.findByColor(color);
    }
}
