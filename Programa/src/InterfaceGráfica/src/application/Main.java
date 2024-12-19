package InterfaceGráfica.src.application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	 @Override
	    public void start(Stage primaryStage) { // Método chamado pelo JavaFX ao iniciar
	        Label label = new Label("Olá, JavaFX!");
	        StackPane root = new StackPane(label);
	        
	        Scene scene = new Scene(root, 1000, 200);
	        primaryStage.setTitle("Teste JavaFX");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args); // Inicializa a aplicação JavaFX
	}
}
