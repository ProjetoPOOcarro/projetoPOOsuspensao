package application;

import org.apache.commons.math3.linear.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class SistemaDeSuspensao extends Application {
	
	Amortecedor Amortecedor;
	Mola MolaSuspensao;
	Mola MolaPneu;
	Massa MassaSuspensa;
	Massa MassaNaoSuspensa;
	Estrada Estrada;
	double DeslocamentoMAX_SUS;
	double DeslocamentoMAX_N_SUS;
	
	//Deixei as siglas para auxiliar nos cálculos

	double m_s = MassaSuspensa.getMassa(); // Massa suspensa (kg)
    double m_u = MassaNaoSuspensa.getMassa();  // Massa não suspensa (kg)
    double k_s = MolaSuspensao.getConstanteK(); // Rigidez da suspensão (N/m)
    double k_t = MolaPneu.getConstanteK(); // Rigidez do pneu (N/m)
    double c_s = Amortecedor.getConstanteC(); // Amortecimento da suspensão (Ns/m)
    double A_sin = Estrada.getAmplitude(); //Amplitude da estrada
    double dt = 0.01; // Passo de tempo (s)
    int steps = 500; // Número de passos (5 s com passo de 0,01 s)
    
    //Setta os parametros do sistema de suspensão
    public void setParametros(Amortecedor Amortecedor, Mola MolaSuspensao, Mola MolaPneu, Massa MassaSuspensa, Massa MassaNaoSuspensa) {
        this.Amortecedor = Amortecedor;
        this.MolaSuspensao = MolaSuspensao;
        this.MolaPneu = MolaPneu;
        this.MassaSuspensa = MassaSuspensa;
        this.MassaNaoSuspensa = MassaNaoSuspensa;
    }

    public static void main(String[] args) {
        // Lançando a aplicação JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        // Matrizes do espaço de estados
        double[][] A = {
                {0, 1, 0, 0},
                {-k_s / m_s, -c_s / m_s, k_s / m_s, c_s / m_s},
                {0, 0, 0, 1},
                {k_s / m_u, c_s / m_u, -(k_s + k_t) / m_u, -c_s / m_u}
        };
        double[][] B = {
                {0},
                {0},
                {0},
                {k_t / m_u}
        };
        double[][] C = {
                {1, 0, 0, 0},
                {0, 0, 1, 0}
        };

        // Vetores de tempo e entrada
        double[] Tempo = new double[steps];
        double[] Oscilação = new double[steps];
        for (int i = 0; i < steps; i++) {
            Tempo[i] = i * dt;
            Oscilação[i] = Estrada.OscilacaoEstrada(Tempo[i]);
        }

        // Estado inicial
        double[] x = {0, 0, 0, 0};
        double[][] Deslocamento = new double[steps][2];

        // Simulação (Método de Runge-Kutta de 4ª ordem)
        RealMatrix AMatrix = new Array2DRowRealMatrix(A);
        RealMatrix BMatrix = new Array2DRowRealMatrix(B);
        RealMatrix CMatrix = new Array2DRowRealMatrix(C);

        for (int i = 0; i < steps; i++) {
            RealMatrix xMatrix = new Array2DRowRealMatrix(x);
            RealMatrix OscilaçãoMatrix = new Array2DRowRealMatrix(new double[]{Oscilação[i]});

            // Método de Runge-Kutta de 4ª ordem
            RealMatrix k1 = CalcularDerivada(AMatrix, BMatrix, xMatrix, OscilaçãoMatrix);
            RealMatrix k2 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k1.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k3 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k2.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k4 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k3.scalarMultiply(dt)), OscilaçãoMatrix);

            RealMatrix dx = k1.add(k2.scalarMultiply(2)).add(k3.scalarMultiply(2)).add(k4).scalarMultiply(dt / 6);

            // Atualiza o estado
            x = xMatrix.add(dx).getColumn(0);

            // Saída
            RealMatrix DeslocamentoMatrix = CMatrix.multiply(new Array2DRowRealMatrix(x));
            
            Deslocamento[i][0] = DeslocamentoMatrix.getEntry(0, 0); // Deslocamento da massa suspensa
            MassaSuspensa.setPosição(Deslocamento[i][0]);
            Deslocamento[i][1] = DeslocamentoMatrix.getEntry(1, 0); // Deslocamento da massa não suspensa
            MassaNaoSuspensa.setPosição(Deslocamento[i][1]);

            if (DeslocamentoMAX_SUS < Deslocamento[i][0]){
            	DeslocamentoMAX_SUS = Deslocamento[i][0];
            }
            
            if (DeslocamentoMAX_N_SUS < Deslocamento[i][1]) {
            	DeslocamentoMAX_N_SUS = Deslocamento[i][1];
            }
        }
    }
    // Função para calcular dx/dt
    private static RealMatrix CalcularDerivada(RealMatrix A, RealMatrix B, RealMatrix x, RealMatrix Oscilação) {
        return A.multiply(x).add(B.multiply(Oscilação));
    }

}
