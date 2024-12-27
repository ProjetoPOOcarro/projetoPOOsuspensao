package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulador {
    private static String[] Textos = {
        "Digite o valor da rigidez da mola (N/m):",
        "Digite o valor da massa suspensa (kg):",
        "Digite o valor da massa não suspensa (kg):",
        "Digite o coeficiente de amortecimento (Ns/m):",
        "Digite a rigidez do pneu (N/m):",
        "Digite o deslocamento imputado pelo solo (m):"
    };

    private final float[] valores = new float[6];
    private int contador = 0;

    public void atribuirValor(int aux, float valor) {
        valores[aux] = valor;
        contador++;
    }

    public boolean todosValoresDefinidos() {
        return contador == 6;
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
}
