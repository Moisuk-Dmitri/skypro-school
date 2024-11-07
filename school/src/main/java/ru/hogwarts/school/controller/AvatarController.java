package ru.hogwarts.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.Collection;

@RestController
@RequestMapping("avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping
    public ResponseEntity<Avatar> createAvatar(@Valid @RequestBody Avatar avatar) {
        return ResponseEntity.ok(avatarService.createAvatar(avatar));
    }

    @GetMapping("{id}")
    public Avatar readAvatar(@PathVariable Long id) {
        return avatarService.readAvatar(id);
    }

    @GetMapping
    public Collection<Avatar> readAllAvatars() {
        return avatarService.readAllAvatars();
    }

    @PutMapping
    public ResponseEntity<Avatar> putAvatar(@RequestBody @Valid Avatar avatar) {
        return ResponseEntity.ok(avatarService.updateAvatar(avatar));
    }

    @DeleteMapping("{id}")
    public void deleteAvatar(@PathVariable Long id) {
        avatarService.deleteAvatar(id);
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) {
        avatarService.uploadAvatar(studentId, avatar);
    }
}
