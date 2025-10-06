package br.com.fecaf.view;

import br.com.fecaf.controller.ProdutoController;
import br.com.fecaf.model.Produto;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {

    // criação dos produtos
    Produto[] produtos = {
            new Produto(
                    "Produto Moderno 1",
                    "/images/produto_1.jpg",
                    "Uma descrição detalhada sobre o Produto Moderno 1. Feito com materiais de alta qualidade para garantir durabilidade e estilo.",
                    199.90
            ),
            new Produto(
                    "Produto Elegante 2",
                    "/images/produto_2.jpg",
                    "O Produto Elegante 2 combina funcionalidade e design sofisticado. Perfeito para quem busca um toque de classe.",
                    249.90
            ),
            new Produto(
                    "Produto Minimalista 3",
                    "/images/produto_1.jpg",
                    "Com um design limpo e focado no essencial, o Produto Minimalista 3 é a escolha ideal para ambientes modernos.",
                    149.90
            )
    };

    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showHome() {
        ProdutoView home = new ProdutoView(new ProdutoController());
        home.adicionarProduto(produtos);
        Scene scene = new Scene(home.getLayout(), 1366, 768);

        // linka o CSS na scene
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm()
        );

        stage.setScene(scene);
    }

    public void showProdutos(Produto produto) {
        DetalhesView detalhesProduto = new DetalhesView(produto);
        stage.setScene(new Scene(detalhesProduto.getLayout(), 1366, 768));

    }
}

