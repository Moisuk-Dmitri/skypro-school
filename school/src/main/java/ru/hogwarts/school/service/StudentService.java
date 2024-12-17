package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Student create method invoked");

        return studentRepository.save(student);
    }

    public Student readStudent(Long id) {
        logger.info("Student read method invoked");

        return studentRepository.findById(id).orElseThrow(WrongIndexException::new);
    }

    public Collection<Student> readAllStudents() {
        logger.info("Student read all method invoked");

        if (studentRepository.count() == 0) {
            logger.error("Students database is empty");
            throw new NoStudentsException();
        }

        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        logger.info("Student update method invoked");

        if (!studentRepository.existsById(student.getId())) {
            logger.error("Student count not be found");
            throw new WrongIndexException();
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Student delete method invoked");

        if (!studentRepository.existsById(id)) {
            logger.error("Student count not be found");
            throw new WrongIndexException();
        }

        studentRepository.deleteById(id);
    }

    public Collection<Student> filterStudentsByAge(int age) {
        logger.info("Student filter by age method invoked");

        return studentRepository.findByAge(age);
    }

    public Collection<Student> filterByAgeBetween(int min, int max) {
        logger.info("Student filter by age between method invoked");

        if (min > max) {
            logger.error("Maximum age must be less or equal minimal");
            throw new AgeMinAboveMaxException();
        }

        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        logger.info("Student get faculty method invoked");

        Student student = studentRepository.findById(id).orElseThrow(NoStudentsException::new);

        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.error("Faculty does not exist");
            throw new NoFacultiesException();
        }

        return faculty;
    }

    public Long countAllStudents() {
        logger.info("Student count all students method invoked");

        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Student get average age method invoked");

        return studentRepository.getAverageAge();
    }

    public Collection<Student> findFiveLast() {
        logger.info("Student find five last method invoked");

        return studentRepository.findFiveLast();
    }

    public Collection<String> findStudentsNameStartWithA() {
        logger.info("Student find students name start with A method invoked");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(e -> e.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }

    public Double getAverageAgeV2() {
        logger.info("Student get average age v2 method invoked");

        return studentRepository.findAll().stream()
                .collect(Collectors.averagingDouble(Student::getAge));
    }

    public Void printNamesParallel() {
        logger.info("Student print names parallel method invoked");

        List<Student> studentList = studentRepository.findAll();

        System.out.println(studentList.get(0).getName());
        System.out.println(studentList.get(1).getName());

        new Thread(() -> {
            System.out.println(studentList.get(2).getName());
            System.out.println(studentList.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(studentList.get(4).getName());
            System.out.println(studentList.get(5).getName());
        }).start();

        return null;
    }

    public Void printNamesSynchronized() {
        logger.info("Student print names parallel method invoked");

        List<Student> studentList = studentRepository.findAll();

        printName(studentList, 0);
        printName(studentList, 1);

        new Thread(() -> {
            printName(studentList, 2);
            printName(studentList, 3);
        }).start();

        new Thread(() -> {
            printName(studentList, 4);
            printName(studentList, 5);
        }).start();

        return null;
    }

    private synchronized void printName(List<Student> students, Integer id) {
        System.out.println(students.get(id).getName());
    }
}
