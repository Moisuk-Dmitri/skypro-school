package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;
import ru.hogwarts.school.service.InfoServiceImpl;

@RestController("info")
public class InfoController {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("port")
    public ResponseEntity<String> getPort() {
        return ResponseEntity.ok(infoService.getPort());
    }
}
