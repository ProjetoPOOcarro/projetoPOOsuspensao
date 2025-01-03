package application;

import org.apache.commons.math3.linear.*;

public class SistemaDeSuspensao {

    Amortecedor Amortecedor;
    Mola MolaSuspensao;
    Mola MolaPneu;
    Massa MassaSuspensa;
    Massa MassaNaoSuspensa;
    Estrada Estrada;
    private double DeslocamentoMAX_SUS = 0;
    private double DeslocamentoMAX_N_SUS = 0;

    // Deixei as siglas para auxiliar nos cálculos
    double m_s;
    double m_u;
    double k_s;
    double k_t;
    double c_s;
    double A_sin;
    double dt = 0.01; // Passo de tempo (s)
    int steps = 500; // Número de passos (5 s com passo de 0,01 s)

    // Seta os parâmetros do sistema de suspensão
    public void setParametros(Amortecedor Amortecedor, Mola MolaSuspensao, Mola MolaPneu, Massa MassaSuspensa, Massa MassaNaoSuspensa, Estrada Estrada) {
        this.Amortecedor = Amortecedor;
        this.MolaSuspensao = MolaSuspensao;
        this.MolaPneu = MolaPneu;
        this.MassaSuspensa = MassaSuspensa;
        this.MassaNaoSuspensa = MassaNaoSuspensa;
        this.Estrada = Estrada;

        // Inicializa os valores com base nos objetos fornecidos
        this.m_s = MassaSuspensa.getMassa();
        this.m_u = MassaNaoSuspensa.getMassa();
        this.k_s = MolaSuspensao.getConstanteK();
        this.k_t = MolaPneu.getConstanteK();
        this.c_s = Amortecedor.getConstanteC();
        this.A_sin = Estrada.getAmplitude();
    }

    public void Calcular() {
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

        double[] Tempo = new double[steps];
        double[] Oscilação = new double[steps];
        for (int i = 0; i < steps; i++) {
            Tempo[i] = i * dt;
            Oscilação[i] = Estrada.OscilacaoEstrada(Tempo[i]);
        }

        double[] x = {0, 0, 0, 0};
        double[][] Deslocamento = new double[steps][2];

        RealMatrix AMatrix = new Array2DRowRealMatrix(A);
        RealMatrix BMatrix = new Array2DRowRealMatrix(B);
        RealMatrix CMatrix = new Array2DRowRealMatrix(C);

        for (int i = 0; i < steps; i++) {
            RealMatrix xMatrix = new Array2DRowRealMatrix(x);
            RealMatrix OscilaçãoMatrix = new Array2DRowRealMatrix(new double[]{Oscilação[i]});

            RealMatrix k1 = CalcularDerivada(AMatrix, BMatrix, xMatrix, OscilaçãoMatrix);
            RealMatrix k2 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k1.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k3 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k2.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k4 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k3.scalarMultiply(dt)), OscilaçãoMatrix);

            RealMatrix dx = k1.add(k2.scalarMultiply(2)).add(k3.scalarMultiply(2)).add(k4).scalarMultiply(dt / 6);

            x = xMatrix.add(dx).getColumn(0);

            RealMatrix DeslocamentoMatrix = CMatrix.multiply(new Array2DRowRealMatrix(x));

            Deslocamento[i][0] = DeslocamentoMatrix.getEntry(0, 0);
            MassaSuspensa.setPosição(Deslocamento[i][0]);
            Deslocamento[i][1] = DeslocamentoMatrix.getEntry(1, 0);
            MassaNaoSuspensa.setPosição(Deslocamento[i][1]);

            if (getDeslocamentoMAX_SUS() < Deslocamento[i][0]) {
            	setDeslocamentoMAX_SUS(Deslocamento[i][0]);
            }

            if (getDeslocamentoMAX_N_SUS() < Deslocamento[i][1]) {
            	setDeslocamentoMAX_N_SUS(Deslocamento[i][1]);
            }
        }
    }

    private static RealMatrix CalcularDerivada(RealMatrix A, RealMatrix B, RealMatrix x, RealMatrix Oscilação) {
        return A.multiply(x).add(B.multiply(Oscilação));
    }
    
    //Getter do Deslocamento Maximo da massa suspensa
    public double getDeslocamentoMAX_SUS() {
        return DeslocamentoMAX_SUS;
    }
    //Setter do Deslocamento Maximo da massa suspensa
    public void setDeslocamentoMAX_SUS(double deslocamentoMAX_SUS) {
        this.DeslocamentoMAX_SUS = deslocamentoMAX_SUS;
    }
    //Getter do Deslocamento Maximo da massa não suspensa
    public double getDeslocamentoMAX_N_SUS() {
        return DeslocamentoMAX_N_SUS;
    }
    //Setter do Deslocamento Maximo da massa não suspensa
    public void setDeslocamentoMAX_N_SUS(double deslocamentoMAX_N_SUS) {
        this.DeslocamentoMAX_N_SUS = deslocamentoMAX_N_SUS;
    }
}
