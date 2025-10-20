package br.com.fecaf.arduino.controller;

import br.com.fecaf.arduino.config.ArduinoSerial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private int numPedido = 0;

    @Autowired
    private ArduinoSerial arduino;

    @GetMapping(value = "/{numero}")
    public int separarPedido(@PathVariable("numero") int numero) {

        System.out.println("Número recebido pela API: " + numero);

        // Enviar a mensagem para o Arduino
        arduino.enviarMensagem(String.valueOf(numero));

        // Retornar a quantidades de chamadas de separação
        numPedido++;
        System.out.println("Este é o pedido de número: " + numPedido);
        return numPedido;
    }
}