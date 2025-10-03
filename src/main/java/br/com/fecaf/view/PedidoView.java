package br.com.fecaf.view;

import br.com.fecaf.controller.PedidoController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PedidoView {

    private VBox layout;
    private TextField txtCliente;
    private TextField txtProduto;
    private Button btnEnviar;

    public PedidoView(PedidoController controller) {
        Label titulo = new Label("Cadastro de Pedido");

        // criando elementos que irão compor a view
        txtCliente = new TextField();
        txtCliente.setPromptText("Nome do Cliente");

        txtProduto = new TextField();
        txtProduto.setPromptText("Produto");

        btnEnviar = new Button("Enviar Pedido");

        // posicionando elementos dentro da VBox (espaçamento de 10)
        layout = new VBox(10, titulo, txtCliente, txtProduto, btnEnviar);

        // aplicando estilização
        layout.getStyleClass().add("layout");

        // conecta controller
        btnEnviar.setOnAction(e -> {
            controller.enviarPedido(txtCliente.getText(), txtProduto.getText());
        });
    }

    // getter para o app lançar o layout
    public VBox getLayout() {
        return layout;
    }

}

