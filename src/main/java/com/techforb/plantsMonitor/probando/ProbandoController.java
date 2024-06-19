package com.techforb.plantsMonitor.probando;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prueba")
@CrossOrigin
public class ProbandoController {

    @GetMapping
    public String holaMundo() {
        return "Hola mundo";
    }
}
