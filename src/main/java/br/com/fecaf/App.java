package br.com.fecaf;

import br.com.fecaf.controller.PedidoController;
import br.com.fecaf.view.PedidoView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // cria a view
        PedidoView view = new PedidoView(new PedidoController());

        // armazena o layout da view na scene
        Scene scene = new Scene(view.getLayout(), 400, 300);

        // linka o CSS na scene
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );

        stage.setTitle("Sistema de Pedidos - Robô");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
