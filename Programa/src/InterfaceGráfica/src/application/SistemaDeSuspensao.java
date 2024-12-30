//Antes de usar
//(tirar do comentario): 21 - 30, 77, 108, 110, 
//(deixar comentado):  41-49, 76, 113(opcional)
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
	
	//Deixei as siglas para auxiliar nos cálculos
	
	/*
	double m_s = MassaSuspensa.massa; // Massa suspensa (kg)
    double m_u = MassaNaoSuspensa.massa;  // Massa não suspensa (kg)
    double k_s = MolaSuspensao.ConstanteK; // Rigidez da suspensão (N/m)
    double k_t = MolaPneu.ConstanteK; // Rigidez do pneu (N/m)
    double c_s = Amortecedor.ConstanteC; // Amortecimento da suspensão (Ns/m)
    double A_sin = Estrada.Amplitude; //Amplitude da estrada
    double dt = 0.01; // Passo de tempo (s)
    int steps = 500; // Número de passos (5 s com passo de 0,01 s)
    */
    
    public static void main(String[] args) {
        // Lançando a aplicação JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) {
    	
        // Parâmetros do sistema
        double m_s = 250.0; // Massa suspensa (kg)
        double m_u = 50.0;  // Massa não suspensa (kg)
        double k_s = 15000.0; // Rigidez da suspensão (N/m)
        double k_t = 200000.0; // Rigidez do pneu (N/m)
        double c_s = 1000.0; // Amortecimento da suspensão (Ns/m)
        // Parâmetros da simulação
        double A_sin = 0.1; // Amplitude do solavanco
        double dt = 0.01; // Passo de tempo (s)
        int steps = 500; // Número de passos (5 s com passo de 0,01 s)

        
        
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
        double[] t = new double[steps];
        double[] u = new double[steps];
        for (int i = 0; i < steps; i++) {
            t[i] = i * dt;
            u[i] = A_sin * Math.sin(2 * Math.PI * t[i]);
            //u[i] = OscilacaoEstrada(t[i]);
        }

        // Estado inicial
        double[] x = {0, 0, 0, 0};
        double[][] y = new double[steps][2];

        // Simulação (Método de Runge-Kutta de 4ª ordem)
        RealMatrix AMatrix = new Array2DRowRealMatrix(A);
        RealMatrix BMatrix = new Array2DRowRealMatrix(B);
        RealMatrix CMatrix = new Array2DRowRealMatrix(C);

        for (int i = 0; i < steps; i++) {
            RealMatrix xMatrix = new Array2DRowRealMatrix(x);
            RealMatrix uMatrix = new Array2DRowRealMatrix(new double[]{u[i]});

            // Método de Runge-Kutta de 4ª ordem
            RealMatrix k1 = calculateDx(AMatrix, BMatrix, xMatrix, uMatrix);
            RealMatrix k2 = calculateDx(AMatrix, BMatrix, xMatrix.add(k1.scalarMultiply(dt / 2)), uMatrix);
            RealMatrix k3 = calculateDx(AMatrix, BMatrix, xMatrix.add(k2.scalarMultiply(dt / 2)), uMatrix);
            RealMatrix k4 = calculateDx(AMatrix, BMatrix, xMatrix.add(k3.scalarMultiply(dt)), uMatrix);

            RealMatrix dx = k1.add(k2.scalarMultiply(2)).add(k3.scalarMultiply(2)).add(k4).scalarMultiply(dt / 6);

            // Atualiza o estado
            x = xMatrix.add(dx).getColumn(0);

            // Saída
            RealMatrix yMatrix = CMatrix.multiply(new Array2DRowRealMatrix(x));
            
            y[i][0] = yMatrix.getEntry(0, 0); // Deslocamento da massa suspensa
            //MassaSuspensa.setPosição(y[i][0]);
            y[i][1] = yMatrix.getEntry(1, 0); // Deslocamento da massa não suspensa
            //MassaNaoSuspensa.setPosição(y[i][1]);

            // Imprime os deslocamentos
            System.out.printf("Step %d: Massa Suspensa = %.5f m, Massa Não Suspensa = %.5f m\n", i, y[i][0], y[i][1]);
        }

        System.out.println("Simulação concluída!");
    }

    // Função para calcular dx/dt
    private static RealMatrix calculateDx(RealMatrix A, RealMatrix B, RealMatrix x, RealMatrix u) {
        return A.multiply(x).add(B.multiply(u));
    }
}
