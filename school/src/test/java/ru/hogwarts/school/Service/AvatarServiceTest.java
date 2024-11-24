package ru.hogwarts.school.Service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPageContext;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("Положительный тест на загрузку изображения из локальной папки")
    @Test
    public void shouldDownloadAvatarFromLocal() {
        Avatar avatar = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        avatar.setData(new byte[0]);
        avatar.setFilePath("src/test/image/Student{id=null, name='Oleg', age=20}.jpg");
        avatar.setFileSize(0L);

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(avatarRepositoryMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));

        avatarService.downloadAvatar(anyLong(), response);
    }

    @DisplayName("Положительный тест на пагинацию изображений")
    @Test
    public void shouldPaginateAvatars() {
        Avatar avatar = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        avatar.setData(new byte[0]);
        avatar.setFilePath("src/test/image/Student{id=null, name='Oleg', age=20}.jpg");
        avatar.setFileSize(0L);

        Avatar avatar1 = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        avatar.setData(new byte[0]);
        avatar.setFilePath("src/test/image/123.jpg");
        avatar.setFileSize(0L);

        when(avatarRepositoryMock.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(avatar, avatar1)));

        assertThat(avatarService.readAvatarsByPages(1,2))
                .isEqualTo(List.of(avatar, avatar1));
    }
}
