package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

class PrimeiraTela {
	private double valor;
	private boolean importar = false;
    private TextField[] caixasDeTexto = new TextField[5];
    private Button[] botoes = new Button[5];
    private Button botão = new Button();
    private Button botãoS = new Button();
    private Button botãoN = new Button();
    Simulador simulador = new Simulador();
    Pane painel = new Pane();
    
    public Scene criarCena(Stage primeriaTela) {
        Label titulo = new Label("|SIMULADOR DE UM QUARTO DE SUSPENSÃO VEICULAR|");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");
        titulo.setLayoutX(10);
        titulo.setLayoutY(10);
        painel.getChildren().add(titulo);
        
        botão = new Button("Sim");
        botão.setLayoutX(450);
        botão.setLayoutY(300);
        
        botãoS = new Button("Sim");
        botãoS.setLayoutX(450);
        botãoS.setLayoutY(260);
        
        botãoN = new Button("Não");
        botãoN.setLayoutX(410);
        botãoN.setLayoutY(260);
        
        Label textoR = new Label("Você deseja importar um arquivo com os dados?");
        textoR.setStyle("-fx-font-size: 15px; -fx-font-family: 'Arial';");
        textoR.setLayoutX(20);
        textoR.setLayoutY(305);
        
        Label textoB = new Label("Você deseja salvar esses dados em um arquivo?");
        textoB.setStyle("-fx-font-size: 15px; -fx-font-family: 'Arial';");
        textoB.setLayoutX(20);
        textoB.setLayoutY(260);
        
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
            configurarBotao(i, primeriaTela);

            painel.getChildren().addAll(textoCaixa, caixasDeTexto[i], botoes[i]);
        }
        painel.getChildren().addAll(botão,textoR,botãoS,botãoN,textoB);
        return new Scene(painel, 580, 360);
    }

    private void configurarBotao(int aux, Stage primeriaTela) {
    	botoes[aux].setOnAction(event -> {
            try {
                valor = Double.parseDouble(caixasDeTexto[aux].getText());
                if(valor<=0) {
                	caixasDeTexto[aux].setStyle("-fx-border-color: red;");
                }
                else {
                	 simulador.atribuirValor(aux, valor);
                     caixasDeTexto[aux].setStyle("-fx-border-color: green;");
                }
            } 
            catch (NumberFormatException erro) {
                caixasDeTexto[aux].setStyle("-fx-border-color: red;");
            }
        });
        botão.setOnAction(event->{
        	primeriaTela.close();
        	simulador.abrirTelaDeImportação(importar);
        });
        botãoS.setOnAction(event->{
        	if(simulador.TodosOsValoresPreenchidos()) {
        		importar = true;
        		primeriaTela.close();
        		simulador.abrirTelaDeImportação(importar);
        	}
        });
        botãoN.setOnAction(event->{
        	if(simulador.TodosOsValoresPreenchidos()) {
        		primeriaTela.close();
        		simulador.abrirSegundaTela();
        	}
        });
    }
}
