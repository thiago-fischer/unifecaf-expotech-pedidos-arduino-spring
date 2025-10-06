package br.com.fecaf.view;

import br.com.fecaf.controller.ProdutoController;
import br.com.fecaf.model.Produto;
import br.com.fecaf.view.components.ProdutoCard;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProdutoView {

    private VBox layout;
    private HBox cardProdutos = new HBox(10);

    public ProdutoView(ProdutoController controller) {
        // header
        HBox header = new HBox();
        header.getStyleClass().add("header");

        Label title = new Label("Nossos Produtos");
        title.getStyleClass().add("header-title");
        header.getChildren().add(title);

        // formatando container dos cards
        cardProdutos.getStyleClass().add("card-container");

        // posicionando elementos dentro da VBox (espaçamento de 10)
        layout = new VBox(10, header, cardProdutos);

        // aplicando estilização
        layout.getStyleClass().add("body");
    }

    // getter para o app lançar o layout
    public VBox getLayout() {
        return layout;
    }

    public void adicionarProduto(Produto[] produtos) {
        for (Produto produto : produtos) {
            ProdutoCard card = new ProdutoCard(produto);
            this.cardProdutos.getChildren().add(card.getCard());
        };
    }

}

