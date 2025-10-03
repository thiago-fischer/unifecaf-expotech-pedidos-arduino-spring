package br.com.fecaf.controller;

import br.com.fecaf.model.Pedido;

public class PedidoController {

    public void enviarPedido(String cliente, String produto) {
        Pedido pedido = new Pedido(cliente, produto);
        System.out.println("Pedido criado: " + pedido);
        // aqui depois você manda pro Arduino
    }
}
