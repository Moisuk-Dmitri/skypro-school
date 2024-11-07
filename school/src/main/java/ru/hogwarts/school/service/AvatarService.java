package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private final Path avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, @Value("${path.avatars}") Path path) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.avatarsDir = path;
    }

    public Avatar createAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    public Avatar readAvatar(Long id) {
        return avatarRepository.findById(id).orElseThrow(NoAvatarsException::new);
    }

    public Collection<Avatar> readAllAvatars() {
        if (avatarRepository.count() == 0) {
            throw new NoFacultiesException();
        }

        return avatarRepository.findAll();
    }

    public Avatar updateAvatar(Avatar avatar) {
        if (!avatarRepository.existsById(avatar.getId())) {
            throw new WrongIndexException();
        }

        return avatarRepository.save(avatar);
    }

    public void deleteAvatar(Long id) {
        if (!avatarRepository.existsById(id)) {
            throw new WrongIndexException();
        }

        avatarRepository.deleteById(id);
    }


    public void uploadAvatar(Long studentId, MultipartFile avatarFile) {
        Student student = studentRepository.findById(studentId).orElseThrow(NoStudentsException::new);
        Path filePath = Path.of(String.valueOf(avatarsDir), student + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));

        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FilesOperationStoppedException();
        }

        try {
            try (
                    InputStream is = avatarFile.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }
        } catch (IOException e) {
            throw new FilesOperationStoppedException();
        }

        try {
            Avatar avatar = readAvatar(studentId);
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setData(avatarFile.getBytes());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new FilesOperationStoppedException();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
