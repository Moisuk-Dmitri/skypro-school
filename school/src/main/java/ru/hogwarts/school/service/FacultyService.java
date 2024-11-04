package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NoFacultiesException;
import ru.hogwarts.school.exception.NoStudentsException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
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
        return facultyRepository.findById(id).orElseThrow(WrongIndexException::new);
    }

    public Collection<Faculty> readAllFaculties() {
        if (facultyRepository.count() == 0) {
            throw new NoFacultiesException();
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

    public Collection<Faculty> filterFacultiesByColorOrName(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }


    public Collection<Student> getStudentsByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(NoFacultiesException::new);

        Collection<Student> students = faculty.getStudents();
        if(students.isEmpty()) {
            throw new NoStudentsException();
        }

        return students;
    }
}
