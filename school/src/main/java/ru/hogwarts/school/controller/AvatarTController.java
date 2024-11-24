package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.Collection;

@RestController
@RequestMapping("avatars")
public class AvatarTController {

    private final AvatarService avatarService;

    public AvatarTController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping()
    public ResponseEntity<Collection<Avatar>> readAvatars(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(avatarService.readAvatarsByPages(pageNumber, pageSize));
    }
}
