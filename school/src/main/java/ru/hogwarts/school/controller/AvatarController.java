package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Avatar> readAvatar(@PathVariable Long id) {
        return ResponseEntity.ok(avatarService.readAvatar(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Avatar>> readAllAvatars() {
        return ResponseEntity.ok(avatarService.readAllAvatars());
    }

    @PutMapping
    public ResponseEntity<Avatar> putAvatar(@RequestBody @Valid Avatar avatar) {
        return ResponseEntity.ok(avatarService.updateAvatar(avatar));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        avatarService.deleteAvatar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) {
        avatarService.uploadAvatar(id, avatar);
    }

    @GetMapping(value = "/avatar-from-db/{id}")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        return avatarService.downloadAvatar(id);
    }

    @GetMapping(value = "/avatar-from-file/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) {
        avatarService.downloadAvatar(id, response);
    }
}
