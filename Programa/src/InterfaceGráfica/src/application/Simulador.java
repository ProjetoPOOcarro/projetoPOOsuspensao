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
    Stage Importar = new Stage();
    public void atribuirValor(int aux, double valor) {
    	//0 valor da rigidez da suspensão (Ks), 1 valor da massa suspensa(Kg), 
    	 //2 valor da massa não suspensa(Kg),3 o coeficiente de amortecimento (Cs),
    	 //4 a rigidez do pneu (Kt)
    	valores[aux] = valor;
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

    public void abrirSegundaTela() {
    	System.out.println(valores[0]);
    	System.out.println(valores[1]);
    	System.out.println(valores[2]);
    	System.out.println(valores[3]);
    	System.out.println(valores[4]);
    	
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
        System.out.println("Vmax=");
        System.out.println(Vmax);
        System.out.println("Vmin=");
        System.out.println(Vmin);
     
        Stage animação = new Stage();
    	animação simulador = new animação();
        Scene cena2 = simulador.criarAnimação(Vmax,Vmin);
        animação.setTitle("Simulador de um quarto de suspensão veicular");
        animação.setScene(cena2);
        animação.setResizable(false); // Desativa o redimensionamento
        animação.show();
        animação.setX(100);
        animação.setY(100);
        
        Stage gráfico1 = new Stage();
        Gráficos dados = new Gráficos();
        Scene cena3 = dados.CriarGráfico1();
        gráfico1.setTitle("Grafico1");
        gráfico1.setScene(cena3);
        gráfico1.setResizable(false); // Desativa o redimensionamento
        gráfico1.show();
        
        gráfico1.setX(681);
        gráfico1.setY(100);
    }
    public void abrirTelaDeImportação(boolean importar) {
    	System.out.println(valores[0]);
    	System.out.println(valores[1]);
    	System.out.println(valores[2]);
    	System.out.println(valores[3]);
    	System.out.println(valores[4]);
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
