package ru.hogwarts.school;

import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {

    private AvatarRepository avatarRepositoryMock = Mockito.mock(AvatarRepository.class);

    private StudentRepository studentRepositoryMock = Mockito.mock(StudentRepository.class);

    private AvatarService avatarService = new AvatarService(
            avatarRepositoryMock,
            studentRepositoryMock,
            Path.of("src/test/image"));

    @DisplayName("Положительный тест на загрузку изображения в базу данных и локальную папку")
    @Test
    public void shouldUploadAvatar() {
        Student student = new Student("Oleg", 20);
        Avatar avatar = new Avatar();

        when(studentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(student));
        when(avatarRepositoryMock.save(avatar)).thenReturn(null);
        when(avatarRepositoryMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));

        MockMultipartFile multipartFile = new MockMultipartFile(
                "test",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        avatarService.uploadAvatar(anyLong(), multipartFile);
    }

    @DisplayName("Положительный тест на загрузку изображения из базы данных")
    @Test
    public void shouldDownloadAvatarFromDB() {
        Avatar avatar = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        avatar.setData(new byte[0]);

        when(avatarRepositoryMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));

        avatarService.downloadAvatar(anyLong());
    }

//    @DisplayName("Положительный тест на загрузку изображения из локальной папки")
//    @Test
//    public void shouldDownloadAvatarFromLocal() {
//        Avatar avatar = new Avatar();
//        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
//        avatar.setData(new byte[0]);
//        avatar.setFilePath("src/test/image");
//        avatar.setFileSize(0L);
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        when(avatarRepositoryMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));
//
//        avatarService.downloadAvatar(anyLong(), response);
//    }
}
