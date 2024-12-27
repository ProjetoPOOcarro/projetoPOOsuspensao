package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

class PrimeiraTela {
    private TextField[] caixasDeTexto = new TextField[5];
    private Button[] botoes = new Button[5];
    Simulador simulador = new Simulador();


    public Scene criarCena(Stage primaryStage) {
        Pane painel = new Pane();
        Label titulo = new Label("|SIMULADOR DE UM QUARTO DE SUSPENSÃO VEICULAR |");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");
        titulo.setLayoutX(10);
        titulo.setLayoutY(10);
        painel.getChildren().add(titulo);

        for (int i = 0; i < 5; i++) {
        	int aux=i;
            Label textoCaixa = new Label(Simulador.getTexto(aux));
            textoCaixa.setStyle("-fx-font-size: 12px; -fx-font-family: 'Arial';");
            textoCaixa.setLayoutX(20);
            textoCaixa.setLayoutY(60 + i * 40);

            caixasDeTexto[i] = new TextField();
            caixasDeTexto[i].setLayoutX(300);
            caixasDeTexto[i].setLayoutY(56 + i * 40);

            botoes[i] = new Button("Ok");
            botoes[i].setLayoutX(450);
            botoes[i].setLayoutY(56 + i * 40);
            configurarBotao(i, primaryStage);

            painel.getChildren().addAll(textoCaixa, caixasDeTexto[i], botoes[i]);
        }
        return new Scene(painel, 580, 360);
    }

    private void configurarBotao(int aux, Stage primaryStage) {
        botoes[aux].setOnAction(event -> {
            try {
                double valor = Double.parseDouble(caixasDeTexto[aux].getText());
                simulador.atribuirValor(aux, valor);
                caixasDeTexto[aux].setStyle("-fx-border-color: green;");
                if (simulador.todosValoresDefinidos()) {
                	configurarVariáveis();
                    primaryStage.close();
                    simulador.abrirSegundaTela(primaryStage);
                }
            } catch (NumberFormatException erro) {
                caixasDeTexto[aux].setStyle("-fx-border-color: red;");
            }
        });
    }
    private void configurarVariáveis() {
    	Amortecedor ConstanteC = new Amortecedor();
    	ConstanteC.setAmortecedor();
    }
}
