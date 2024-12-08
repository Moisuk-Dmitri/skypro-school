package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private final Path avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, @Value("${path.avatars}") Path path) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.avatarsDir = path;
    }

    public Avatar createAvatar(Avatar avatar) {
        logger.info("Avatar create method invoked");

        return avatarRepository.save(avatar);
    }

    public Avatar readAvatar(Long id) {
        logger.info("Avatar read method invoked");

        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

    public Collection<Avatar> readAllAvatars() {
        logger.info("Avatar read all method invoked");

        if (avatarRepository.count() == 0) {
            logger.error("Avatar database is empty");
            throw new NoAvatarsException();
        }

        return avatarRepository.findAll();
    }

    public Avatar updateAvatar(Avatar avatar) {
        logger.info("Avatar update method invoked");

        if (!avatarRepository.existsById(avatar.getId())) {
            logger.error("Avatar cant be found");
            throw new WrongIndexException();
        }

        return avatarRepository.save(avatar);
    }

    public void deleteAvatar(Long id) {
        logger.info("Avatar delete method invoked");

        if (!avatarRepository.existsById(id)) {
            logger.error("Avatar cant be found");
            throw new WrongIndexException();
        }

        avatarRepository.deleteById(id);
    }


    public void uploadAvatar(Long studentId, MultipartFile avatarFile) {
        logger.info("Avatar upload method invoked");

        Student student = studentRepository.findById(studentId).orElseThrow(NoStudentsException::new);
        Path filePath = Path.of(String.valueOf(avatarsDir), student + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));

        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Could not create directory");
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
            logger.error("Could not create I/O stream");
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
            logger.error("Avatar could not be saved");
            throw new FilesOperationStoppedException();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public ResponseEntity<byte[]> downloadAvatar(Long studentId) {
        logger.info("Avatar download into database method invoked");

        Avatar avatar = readAvatar(studentId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    public void downloadAvatar(Long id, HttpServletResponse response) {
        logger.info("Avatar download into local binary method invoked");

        Avatar avatar = readAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try {
            try (InputStream is = Files.newInputStream(path);
                 OutputStream os = response.getOutputStream();) {
                response.setStatus(200);
                response.setContentType(avatar.getMediaType());
                response.setContentLength(Math.toIntExact(avatar.getFileSize()));
                is.transferTo(os);
            }
        } catch (IOException e) {
            logger.error("Could not create I/O stream");
            throw new FilesOperationStoppedException();
        }
    }

    public Collection<Avatar> readAvatarsByPages(Integer pageNumber, Integer pageSize) {
        logger.info("Avatar read by pages method invoked");

        if (pageNumber <= 0 || pageSize <= 0) {
            logger.error("Page number or page size cant be less or equal zero");
            throw new PageSettingsUnderZero();
        }

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
