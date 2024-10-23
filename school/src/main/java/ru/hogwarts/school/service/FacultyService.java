package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EmptyColorException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private Long id = 0L;
    private final Map<Long, Faculty> facultyMap = new HashMap<>();

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++id);

        facultyMap.put(faculty.getId(), faculty);

        return faculty;
    }

    public Faculty readFaculty(Long id) {
        if (!facultyMap.containsKey(id)) {
            throw new WrongIndexException();
        }

        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            throw new WrongIndexException();
        }

        facultyMap.put(faculty.getId(), faculty);

        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        if (!facultyMap.containsKey(id)) {
            throw new WrongIndexException();
        }

        return facultyMap.remove(id);
    }

    public Collection<Faculty> filterFacultiesByColor(String color) {
        if (color.isEmpty()) {
            throw new EmptyColorException();
        }

        return facultyMap.values().stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toSet());
    }
}
