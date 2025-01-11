/*
 * Cria os elementos da interface gráfica
 */
package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

class PrimeiraTela {
	private boolean[] botaoClicado = new boolean[5]; // Array para controle de cliques
	private double valor;//amazenar os valores das caixas de texto
	private boolean importar = false;//sinaliza se é necessário criar uma tela para digitar o nome do arquivo cujos dados serão importador
    private TextField[] caixasDeTexto = new TextField[5];//array para amazenar as caixas de texto
    private Button[] botoes = new Button[5];
    private Button botão = new Button();//botão para importar informação de arquivos
    private Button botãoS = new Button();//botão para salvar os dados em arquivos
    private Button botãoN = new Button();//botão para iniciar a simulação direto
    Simulador simulador = new Simulador();
    Pane painel = new Pane();
    
    public Scene criarCena(Stage primeriaTela) {//cria os elementos da tela
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

    private void configurarBotao(int aux, Stage primeriaTela) {//configura a ação de todos os botões
    	botoes[aux].setOnAction(event -> {
    	    if (botaoClicado[aux]==false) { // Apenas executa se ainda não foi clicado
    	        try {
    	            valor = Double.parseDouble(caixasDeTexto[aux].getText());
    	            if (valor <= 0) {
    	                caixasDeTexto[aux].setStyle("-fx-border-color: red;");
    	            } else {
    	                simulador.atribuirValor(aux, valor);
    	                simulador.contador();
    	                caixasDeTexto[aux].setStyle("-fx-border-color: green;");
    	                botaoClicado[aux] = true; // Marca como clicado
    	            }
    	        } catch (NumberFormatException erro) {
    	            caixasDeTexto[aux].setStyle("-fx-border-color: red;");
    	        }
    	    }
    	    else {//caso queira substituir o valor digitado
    	    	try {
    	            valor = Double.parseDouble(caixasDeTexto[aux].getText());
    	            if (valor <= 0) {
    	                caixasDeTexto[aux].setStyle("-fx-border-color: red;");
    	            } else {
    	                simulador.atribuirValor(aux, valor);
    	                caixasDeTexto[aux].setStyle("-fx-border-color: green;");
    	            }
    	        } catch (NumberFormatException erro) {
    	            caixasDeTexto[aux].setStyle("-fx-border-color: red;");
    	        }
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
