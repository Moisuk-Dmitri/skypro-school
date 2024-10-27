package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.exception.EmptyColorException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    public Collection<Faculty> findByColor(String color);
}
