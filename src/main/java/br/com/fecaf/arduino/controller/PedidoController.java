package br.com.fecaf.arduino.controller;

import br.com.fecaf.arduino.config.ArduinoSerial;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private int numPedido = 0;
    private ArduinoSerial arduino = new ArduinoSerial();

    @GetMapping(value = "/{numero}")
    public int separarPedido(@PathVariable("numero") int numero) {

        //Conexão com a porta serial
        System.out.println("Número recebido: " + numero);

        if (arduino.abrirPorta("COM3", 9600)) {
//            arduino.enviarMensagem(String.valueOf(numero));
            arduino.fecharPorta();
        }

        //Retornar a quantidades de chamadas de separação
        numPedido++;
        return numPedido;
    }

}
