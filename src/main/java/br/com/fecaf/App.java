package br.com.fecaf;


import br.com.fecaf.view.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.text.Font;


public class App extends Application {

    public static SceneManager sceneManager;

    @Override
    public void start(Stage stage) {

        // carrega a fonte ANTES de aplicar o CSS
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-SemiBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins/Poppins-Bold.ttf"), 14);


        sceneManager = new SceneManager(stage);
        sceneManager.showHome(); // primeira tela
        stage.setTitle("Nossa Loja");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
