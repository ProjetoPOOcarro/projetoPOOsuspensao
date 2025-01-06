package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ImportaçãoDeArquivos {
    Pane painel = new Pane();
    Simulador simulador = new Simulador();
    Scene cena;
	public Scene criarTelaDeImportarArquivo(boolean importar) {
		if(importar == false) {
			Label textoCaixa = new Label("Digite o nome do arquivo");
			textoCaixa.setStyle("-fx-font-size: 12px; -fx-font-family: 'Arial';");
			textoCaixa.setLayoutX(20);
			textoCaixa.setLayoutY(60);
       
			TextField caixasDeTexto2 = new TextField();
			caixasDeTexto2.setLayoutX(300);
			caixasDeTexto2.setLayoutY(56);
        
			Button botão = new Button();
			botão = new Button("Ok");
			botão.setLayoutX(450);
			botão.setLayoutY(56);
			configurarBotao(botão,caixasDeTexto2,importar);
        
			painel.getChildren().addAll(botão,caixasDeTexto2,textoCaixa);
			cena = new Scene(painel, 580, 320);
		}
		else {
			Label textoCaixa = new Label("Digite o nome do arquivo em que deseje salvar os dados");
			textoCaixa.setStyle("-fx-font-size: 12px; -fx-font-family: 'Arial';");
			textoCaixa.setLayoutX(20);
			textoCaixa.setLayoutY(60);
       
			TextField caixasDeTexto2 = new TextField();
			caixasDeTexto2.setLayoutX(340);
			caixasDeTexto2.setLayoutY(56);
        
			Button botão = new Button();
			botão = new Button("Ok");
			botão.setLayoutX(490);
			botão.setLayoutY(56);
			configurarBotao(botão,caixasDeTexto2,importar);
        
			painel.getChildren().addAll(botão,caixasDeTexto2,textoCaixa);
			cena = new Scene(painel, 580, 320);
		}
		return cena;
	}
	 private void configurarBotao(Button botão,TextField caixasDeTexto2, boolean importar) {
		 if(importar==false) {
			 botão.setOnAction(event -> {
				 try {
					 String nome = caixasDeTexto2.getText();
					 simulador.atribuirValoresImportados(nome);
	            	
				 } 
				 catch (IllegalArgumentException erro) {
	            	caixasDeTexto2.setStyle("-fx-border-color: red;");
				 }
			 });
		 }
		 else {
			 botão.setOnAction(event -> {
				 try {
					 String nome = caixasDeTexto2.getText();
					 simulador.SalvarNoArquivo(nome);
	            	
				 } 
				 catch (IllegalArgumentException erro) {
	            	caixasDeTexto2.setStyle("-fx-border-color: red;");
				 }
			 });
		 }
	}
}
