package br.com.fecaf.view.components;

import br.com.fecaf.model.Produto;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.Objects;

public class ProdutoCard {

    public VBox card = new VBox(5);

    public ProdutoCard(Produto produto) {
        // Imagem
        // Carregar a imagem do classpath (resources)
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(produto.getImagePath())));
        // Criar o ImageView
        ImageView imageView = new ImageView(image);

        // Ajustar tamanho, se necessário
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        // Nome do produto
        Label nameLabel = new Label(produto.getName());

        // Preço do produto
        Label priceLabel = new Label("R$" + String.valueOf(produto.getPrice()));
        priceLabel.getStyleClass().add("price");

        // Botão Detalhes
        Button button = new Button("Ver Detalhes");

        // Criar layout
        this.card.getChildren().addAll(imageView, nameLabel, priceLabel, button);
        this.card.getStyleClass().add("product-card");
    }

    public VBox getCard() {
        return card;
    }
}

