package br.com.fecaf.arduino.controller;

import br.com.fecaf.arduino.service.GrafoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/rotas")
public class RotasController {

    private final GrafoService service;

    public RotasController(GrafoService service) {
        this.service = service;
    }

    @GetMapping("/{origem}/{destino}")
    public List<String> menorRota(@PathVariable String origem,
                                  @PathVariable String destino) {
        return service.calcularMenorRota(origem, destino);
    }
}
