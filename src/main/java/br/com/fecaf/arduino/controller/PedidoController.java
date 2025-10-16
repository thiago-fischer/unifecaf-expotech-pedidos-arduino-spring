package br.com.fecaf.arduino.controller;

import br.com.fecaf.arduino.config.ArduinoSerial;
import org.springframework.beans.factory.annotation.Autowired; // Importe o Autowired
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private int numPedido = 0;

    @Autowired
    private ArduinoSerial arduino;

    @GetMapping(value = "/{numero}")
    public int separarPedido(@PathVariable("numero") int numero) {

        System.out.println("Número recebido pela API: " + numero);

        // Agora não precisamos mais abrir e fechar a porta aqui
        // Apenas enviamos a mensagem
        arduino.enviarMensagem(String.valueOf(numero));

        // Retornar a quantidades de chamadas de separação
        numPedido++;
        System.out.println("Este é o pedido de número: " + numPedido);
        return numPedido;
    }
}