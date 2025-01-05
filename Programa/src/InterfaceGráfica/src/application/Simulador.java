package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Simulador {
	private int contador = 0;
    private static String[] Textos = {
        "Digite o valor da rigidez da suspensão (N/m):",
        "Digite o valor da massa suspensa (kg):",
        "Digite o valor da massa não suspensa (kg):",
        "Digite o coeficiente de amortecimento (Ns/m):",
        "Digite a rigidez do pneu (N/m):"
    };

    private static double[] valores = new double[5];
    Stage Importar = new Stage();
    public void atribuirValor(int aux, double valor) {
    	//0 valor da rigidez da suspensão (Ks), 1 valor da massa suspensa(Kg), 
    	 //2 valor da massa não suspensa(Kg),3 o coeficiente de amortecimento (Cs),
    	 //4 a rigidez do pneu (Kt)
    	valores[aux] = valor;
    	contador++;
    }
    public void atribuirValoresImportados(String nome) {
    	Projeto valor = new Projeto(nome);
    	valores = valor.LerDados();
    	abrirSegundaTela();
    }
    public void SalvarNoArquivo(String nome) {
    	Projeto valor = new Projeto(nome);
    	valor.SalvarDados(valores[0],valores[1],valores[2],valores[3],valores[4]);
    	abrirSegundaTela();
    }
    public boolean TodosOsValoresPreenchidos() {
    	return contador == 5;
    }

    public void abrirSegundaTela() {
    	Mola molaSuspensao = new Mola(); // Usa o valor de rigidez da suspensão
    	molaSuspensao.setConstanteK(valores[0]);
   
        Mola molaPneu = new Mola(); // Usa o valor de rigidez do pneu
        molaPneu.setConstanteK(valores[4]);
        
     // Criação e configuração das massas
        Massa massaSuspensa = new Massa(); // Massa suspensa
        massaSuspensa.setMassa(valores[1]);
        
        Massa massaNaoSuspensa = new Massa(); // Massa não suspensa
        massaNaoSuspensa.setMassa(valores[2]);
        
        Amortecedor amortecedor = new Amortecedor(); // Amortecimento da suspensão
        amortecedor.setConstanteC(valores[3]);
        
        Estrada estrada = new Estrada(0.1);
        
        SistemaDeSuspensao sistema = new SistemaDeSuspensao();
        sistema.setParametros(amortecedor, molaSuspensao, molaPneu, massaSuspensa, massaNaoSuspensa,estrada);
        double Vmax;
        double Vmin;
    	
        sistema.Calcular();
        Vmax=sistema.getDeslocamentoMAX_SUS();
        Vmin=sistema.getDeslocamentoMAX_N_SUS();
     
        Stage animação = new Stage();
    	animação simulador = new animação();
        Scene cena2 = simulador.criarAnimação(Vmax,Vmin);
        animação.setTitle("Simulador de um quarto de suspensão veicular");
        animação.setScene(cena2);
        animação.setResizable(false); // Desativa o redimensionamento
        animação.show();
        animação.setX(100);
        animação.setY(0);
        
        Stage gráfico1 = new Stage();
        Gráficos dados = new Gráficos();
        Scene cena3 = dados.CriarGráfico1(sistema);
        gráfico1.setTitle("Grafico1");
        gráfico1.setScene(cena3);
        gráfico1.setResizable(false); // Desativa o redimensionamento
        gráfico1.show();
        
        gráfico1.setX(681);
        gráfico1.setY(0);
        
        Stage gráfico2 = new Stage();
        Gráficos dados2 = new Gráficos();
        Scene cena4 = dados2.CriarGráfico2(sistema);
        gráfico2.setTitle("Grafico2");
        gráfico2.setScene(cena4);
        gráfico2.setResizable(false); // Desativa o redimensionamento
        gráfico2.show();
        
        gráfico2.setX(100);
        gráfico2.setY(350);
        
        Stage gráfico3 = new Stage();
        Gráficos dados3 = new Gráficos();
        Scene cena5 = dados3.CriarGráfico3(sistema);
        gráfico3.setTitle("Grafico3");
        gráfico3.setScene(cena5);
        gráfico3.setResizable(false); // Desativa o redimensionamento
        gráfico3.show();
        
        gráfico3.setX(681);
        gráfico3.setY(401);
    }
    public void abrirTelaDeImportação(boolean importar) {
    	ImportaçãoDeArquivos simulador = new ImportaçãoDeArquivos();
        Scene cena2 = simulador.criarTelaDeImportarArquivo(importar);
        Importar.setTitle("Simulador de um quarto de suspensão veicular");
        Importar.setScene(cena2);
        Importar.setResizable(false); // Desativa o redimensionamento
        Importar.show();

    }
    public static String getTexto(int aux) {
    	return Textos[aux];
    }
    public static double getValores(int aux) {
    	return valores[aux];
    }
}
