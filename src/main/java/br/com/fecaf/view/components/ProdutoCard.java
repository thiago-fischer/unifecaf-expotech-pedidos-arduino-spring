package br.com.fecaf.view.components;

import br.com.fecaf.App;
import br.com.fecaf.model.Produto;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.Objects;

public class ProdutoCard {

    private final VBox card;

    public ProdutoCard(Produto produto) {
        this.card = new VBox(10); // espaçamento entre elementos
        this.card.getStyleClass().add("product-card"); // classe principal de estilo

        // --- Aplica o CSS específico ---
        this.card.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/styles/produto-card.css")
                ).toExternalForm()
        );

        // --- Imagem ---
        Image image = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(produto.getImagePath())
                )
        );

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("image-view");

        // --- Nome do produto ---
        Label nameLabel = new Label(produto.getName());
        nameLabel.getStyleClass().add("label");

        // --- Preço do produto ---
        Label priceLabel = new Label(String.format("R$ %.2f", produto.getPrice()));
        priceLabel.getStyleClass().add("price");

        // --- Botão Detalhes ---
        Button button = new Button("Ver Detalhes");
        button.setOnAction(e -> App.sceneManager.showProdutos(produto));

        // --- Adiciona os componentes ao layout ---
        this.card.getChildren().addAll(imageView, nameLabel, priceLabel, button);
    }

    public VBox getCard() {
        return card;
    }
}
