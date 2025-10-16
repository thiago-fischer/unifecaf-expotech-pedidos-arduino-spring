package br.com.fecaf.arduino.config;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component // Anotação para o Spring gerenciar esta classe
public class ArduinoSerial {

    private SerialPort portaSerial;
    private final String NOME_PORTA = "COM5";
    private final int BAUD_RATE = 9600;

    @PostConstruct
    public void iniciar() {
        portaSerial = SerialPort.getCommPort(NOME_PORTA);
        portaSerial.setBaudRate(BAUD_RATE);
        portaSerial.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000);

        if (portaSerial.openPort()) {
            System.out.println("Porta " + NOME_PORTA + " aberta com sucesso!");
            // É crucial esperar o Arduino reiniciar após abrir a porta
            try {
                Thread.sleep(2000); // Aumente este tempo se necessário
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Erro ao esperar pela inicialização do Arduino.");
            }
        } else {
            System.err.println("Não foi possível abrir a porta " + NOME_PORTA);
            // Você pode querer lançar uma exceção aqui para impedir a aplicação de iniciar
        }
    }

    // Envia uma mensagem
    public void enviarMensagem(String mensagem) {
        if (portaSerial != null && portaSerial.isOpen()) {
            try {
                String mensagemComQuebraDeLinha = mensagem + "\n";
                portaSerial.getOutputStream().write(mensagemComQuebraDeLinha.getBytes());
                portaSerial.getOutputStream().flush();
                System.out.println("Enviado para o Arduino: " + mensagem);
            } catch (Exception e) {
                System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            }
        } else {
            System.err.println("A porta serial não está aberta para enviar a mensagem.");
        }
    }

    @PreDestroy
    public void fecharPorta() {
        if (portaSerial != null && portaSerial.isOpen()) {
            portaSerial.closePort();
            System.out.println("Porta " + NOME_PORTA + " fechada.");
        }
    }
}