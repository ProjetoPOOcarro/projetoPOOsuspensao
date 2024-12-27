package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        PrimeiraTela interfaceSuspensao = new PrimeiraTela();
        Scene cena = interfaceSuspensao.criarCena(primaryStage);
        primaryStage.setTitle("Simulador de um quarto de suspensão veicular");
        primaryStage.setScene(cena);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
