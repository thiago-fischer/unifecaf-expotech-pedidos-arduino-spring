package br.com.fecaf.view;

import br.com.fecaf.App;
import br.com.fecaf.model.Produto;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Objects;

public class DetalhesView {

    HBox layout = new HBox(10);

    public DetalhesView(Produto produto) {
        // Imagem
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(produto.getImagePath())));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        // Informações
        Label nameLabel = new Label(produto.getName());

        Label descriptionLabel = new Label(produto.getDescription());

        Label priceLabel = new Label(String.valueOf(produto.getPrice()));

        Button sendButton = new Button("Fazer Envio");

        Button backButton = new Button("Voltar para a Loja");
        backButton.setOnAction(e -> App.sceneManager.showHome());

        VBox infoBox = new VBox(10, nameLabel, descriptionLabel, priceLabel, sendButton, backButton);

        layout.getChildren().addAll(imageView, infoBox);
    }

    public HBox getLayout() {
        return layout;
    }
}

