package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student readStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(WrongIndexException::new);
    }

    public Collection<Student> readAllStudents() {
        if (studentRepository.count() == 0) {
            throw new NoStudentsException();
        }

        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new WrongIndexException();
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new WrongIndexException();
        }

        studentRepository.deleteById(id);
    }

    public Collection<Student> filterStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> filterByAgeBetween(int min, int max) {
        if (min > max) {
            throw new AgeMinAboveMaxException();
        }

        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NoStudentsException::new);

        Faculty faculty = student.getFaculty();
        if(faculty == null) {
            throw new NoFacultiesException();
        }

        return faculty;
    }
}
