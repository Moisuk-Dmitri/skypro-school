package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EmptyDatabaseException;
import ru.hogwarts.school.exception.WrongAgeException;
import ru.hogwarts.school.exception.WrongIndexException;
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
        if (!studentRepository.existsById(id)) {
            throw new WrongIndexException();
        }

        return studentRepository.findById(id).get();
    }

    public Collection<Student> readAllStudents() {
        if (studentRepository.count() == 0) {
            throw new EmptyDatabaseException();
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
        if (age <= 0) {
            throw new WrongAgeException();
        }

        return studentRepository.findByAge(age);
    }
}
