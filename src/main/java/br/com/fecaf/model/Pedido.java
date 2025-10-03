package br.com.fecaf.model;

public class Pedido {
    private String cliente;
    private String produto;

    public Pedido(String cliente, String produto) {
        this.cliente = cliente;
        this.produto = produto;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProduto() {
        return produto;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "cliente='" + cliente + '\'' +
                ", produto='" + produto + '\'' +
                '}';
    }
}

