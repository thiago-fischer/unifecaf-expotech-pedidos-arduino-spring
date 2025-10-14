package br.com.fecaf.arduino.config;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerial {

    private SerialPort portaSerial;

    // Abre a porta
    public boolean abrirPorta(String nomePorta, int baudRate) {
        portaSerial = SerialPort.getCommPort(nomePorta);
        portaSerial.setBaudRate(baudRate);

        if (portaSerial.openPort()) {
            System.out.println("Porta " + nomePorta + " aberta com sucesso!");
            try {
                Thread.sleep(1000); // tempo para o Arduino iniciar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        } else {
            System.out.println("Não foi possível abrir a porta " + nomePorta);
            return false;
        }
    }

    // Envia uma mensagem
    public void enviarMensagem(String mensagem) {

        mensagem = mensagem + "/n";

        if (portaSerial != null && portaSerial.isOpen()) {
            try {
                portaSerial.getOutputStream().write(mensagem.getBytes());
                portaSerial.getOutputStream().flush();
                System.out.println("Enviado: " + mensagem.trim());
            } catch (Exception e) {
                System.out.println("Erro ao enviar mensagem: " + e.getMessage());
            }
        } else {
            System.out.println("Porta não está aberta.");
        }
    }

    // Fecha a porta
    public void fecharPorta() {
        if (portaSerial != null && portaSerial.isOpen()) {
            portaSerial.closePort();
            System.out.println("Porta fechada.");
        }
    }
}

