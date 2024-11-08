package ru.hogwarts.school;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {

    private AvatarRepository avatarRepository = Mockito.mock(AvatarRepository.class);

    private StudentRepository studentRepository = Mockito.mock(StudentRepository.class);

    private AvatarService avatarService = new AvatarService(
            avatarRepository,
            studentRepository,
            Path.of("src/test/image"));

    @Test
    public void shouldUploadAvatar() {
        Student student = new Student("Oleg", 20);
        Avatar avatar = new Avatar();

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(avatarRepository.save(avatar)).thenReturn(null);
        when(avatarRepository.findById(anyLong())).thenReturn(Optional.of(avatar));

        MockMultipartFile multipartFile = new MockMultipartFile(
                "test",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        avatarService.uploadAvatar(1L, multipartFile);
    }


}
