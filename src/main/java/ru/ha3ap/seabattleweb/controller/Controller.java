package ru.ha3ap.seabattleweb.controller;


import org.springframework.web.bind.annotation.*;
import ru.ha3ap.seabattleweb.service.Service;

import java.io.IOException;

@RestController
public class Controller {
    private Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/src/main/resources/{file}")
    public byte[] getFile(@PathVariable String file) throws IOException {
        return service.getFile(file);
    }

    @GetMapping("/")
    public String getCoin() throws IOException {
        return service.getCoin();
    }

    @PostMapping("/start")
    public String getStart(@RequestBody String choice) throws IOException {
        return service.getStart(choice);
    }

    @PostMapping("/attack")
    public @ResponseBody String attackWarField(@RequestBody String attack) throws IOException {
        return service.attack(attack);
    }

    @GetMapping("/attack")
    public @ResponseBody String getWarField() throws IOException {
        return service.printField("");
    }

    @GetMapping("/restart")
    public String restart() {
        return service.restart();
    }
}
