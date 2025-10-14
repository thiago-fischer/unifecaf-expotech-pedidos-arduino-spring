package br.com.fecaf.arduino.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private int numPedido = 0;

    @GetMapping(value = "/{numero}")
    public int separarPedido(@PathVariable("numero") int numero) {

        //Implementar as chamadas da lógica de separação do braço robótico aqui
        System.out.println("Número recebido: " + numero);

        //Retornar a quantidades de chamadas de separação
        numPedido++;
        return numPedido;
    }

}
