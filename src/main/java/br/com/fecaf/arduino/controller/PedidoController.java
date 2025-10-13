package br.com.fecaf.arduino.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @GetMapping(value = "/{numero}")
    public int separarPedido(@PathVariable("numero") int numero) {
        System.out.println("Número recebido: " + numero);
        return numero;
    }

}
