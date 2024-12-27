package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulador {
    private static String[] Textos = {
        "Digite o valor da rigidez da suspensão (N/m):",
        "Digite o valor da massa suspensa (kg):",
        "Digite o valor da massa não suspensa (kg):",
        "Digite o coeficiente de amortecimento (Ns/m):",
        "Digite a rigidez do pneu (N/m):"
    };

    private static double[] valores = new double[5];
    private int contador = 0;

    public void atribuirValor(int aux, double valor) {
    	//0 valor da rigidez da suspensão (Ks), 1 valor da massa suspensa(Kg), 
    	 //2 valor da massa não suspensa(Kg),3 o coeficiente de amortecimento (Cs),
    	 //4 a rigidez do pneu (Kt)
    	valores[aux] = valor;
    	contador++;
    
    }

    public boolean todosValoresDefinidos() {
    	return contador == 5;
    }

    public void abrirSegundaTela(Stage animação) {
    	animação simulador = new animação();
        Scene cena2 = simulador.criarAnimação();
        animação.setTitle("Simulador de um quarto de suspensão veicular");
        animação.setScene(cena2);
        animação.setResizable(false); // Desativa o redimensionamento
        animação.show();
    }
    public static String getTexto(int aux) {
    	return Textos[aux];
    }
    public static double getValores(int aux) {
    	return valores[aux];
    }
}
