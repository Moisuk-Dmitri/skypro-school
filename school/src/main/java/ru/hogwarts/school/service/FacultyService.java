package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NoFacultiesException;
import ru.hogwarts.school.exception.NoStudentsException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Faculty create method invoked");

        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(Long id) {
        logger.info("Faculty read method invoked");

        return facultyRepository.findById(id).orElseThrow(WrongIndexException::new);
    }

    public Collection<Faculty> readAllFaculties() {
        logger.info("Faculty read all method invoked");

        if (facultyRepository.count() == 0) {
            logger.error("Faculty database is empty");
            throw new NoFacultiesException();
        }

        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Faculty update method invoked");

        if (!facultyRepository.existsById(faculty.getId())) {
            logger.error("Faculty could not be found");
            throw new WrongIndexException();
        }

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Faculty delete method invoked");

        if (!facultyRepository.existsById(id)) {
            logger.error("Faculty could not be found");
            throw new WrongIndexException();
        }

        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> filterFacultiesByColorOrName(String color, String name) {
        logger.info("Faculty filter by color or name method invoked");

        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }


    public Collection<Student> getStudentsByFacultyId(Long id) {
        logger.info("Faculty get students method invoked");

        Collection<Student> students = facultyRepository.findById(id).orElseThrow(NoFacultiesException::new).getStudents();
        if (students.isEmpty()) {
            logger.error("Students does not exist");
            throw new NoStudentsException();
        }

        return students;
    }
}
