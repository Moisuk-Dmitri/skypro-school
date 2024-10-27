package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.WrongAgeException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private Long id = 0L;
    private final Map<Long, Student> studentMap = new HashMap<>();

    public Student createStudent(Student student) {
        student.setId(++id);
        studentMap.put(student.getId(), student);

        return student;
    }

    public Student readStudent(Long id) {
        if (!studentMap.containsKey(id)) {
            throw new WrongIndexException();
        }

        return studentMap.get(id);
    }

    public Student updateStudent(Student student) {
        if (!studentMap.containsKey(student.getId())) {
            throw new WrongIndexException();
        }

        studentMap.put(student.getId(), student);

        return student;
    }

    public Student deleteStudent(Long id) {
        if (!studentMap.containsKey(id)) {
            throw new WrongIndexException();
        }

        return studentMap.remove(id);
    }

    public Collection<Student> filterStudentsByAge(int age) {
        if (age <= 0) {
            throw new WrongAgeException();
        }

        return studentMap.values().stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toSet());
    }
}
