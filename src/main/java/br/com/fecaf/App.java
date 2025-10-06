package br.com.fecaf;

import br.com.fecaf.controller.ProdutoController;
import br.com.fecaf.model.Produto;
import br.com.fecaf.view.ProdutoView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;
import javafx.scene.text.Font;


public class App extends Application {

    // criação dos produtos
    Produto[] produtos = {
            new Produto(
                    "Produto Moderno 1",
                    "/images/produto_1.jpg",
                    "Uma descrição detalhada sobre o Produto Moderno 1. Feito com materiais de alta qualidade para garantir durabilidade e estilo.",
                    199.9
            ),
            new Produto(
                    "Produto Moderno 2",
                    "/images/produto_2.jpg",
                    "Uma descrição detalhada sobre o Produto Moderno 1. Feito com materiais de alta qualidade para garantir durabilidade e estilo.",
                    199.9
            ),
            new Produto(
                    "Produto Moderno 3",
                    "/images/produto_1.jpg",
                    "Uma descrição detalhada sobre o Produto Moderno 1. Feito com materiais de alta qualidade para garantir durabilidade e estilo.",
                    199.9
            )
    };

    @Override
    public void start(Stage stage) {
        // cria a view
        ProdutoView view = new ProdutoView(new ProdutoController());

        // adicionas os produtos
        view.adicionarProduto(produtos);

        // armazena o layout da view na scene
        Scene scene = new Scene(view.getLayout(), 1366, 768);

        // carrega a fonte ANTES de aplicar o CSS
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-SemiBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-Bold.ttf"), 14);


        // linka o CSS na scene
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );

        stage.setTitle("Nossa Loja");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
