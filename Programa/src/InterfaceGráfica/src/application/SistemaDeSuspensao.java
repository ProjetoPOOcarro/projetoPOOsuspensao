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
    int passo = 1000; // Número de passos (5 s com passo de 0,01 s)
    
    double [][] INF_SISTEMA_SUSPENSAO = new double[passo][5];
    /*
     * [passo][0] INF_SISTEMA_SUSPENSAO = Tempo
     * [passo][1] INF_SISTEMA_SUSPENSAO = Deslocamento da massa suspensa
     * [passo][2] INF_SISTEMA_SUSPENSAO = Deslocamento da massa não suspensa
     * [passo][3] INF_SISTEMA_SUSPENSAO = Deslocamento do sistema (Deslocamento da massa não suspensa - Deslocamento da massa suspensa)
     * [passo][4] INF_SISTEMA_SUSPENSAO = Aceleração da massa suspensa
     */
    

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

        double[] Tempo = new double[passo];
        double[] Oscilação = new double[passo];
        for (int i = 0; i < passo; i++) {
        	INF_SISTEMA_SUSPENSAO [i][0] = i * dt;
        	Tempo[i] = i * dt;
            Oscilação[i] = Estrada.OscilacaoEstrada(Tempo[i]);
        }

        double[] x = {0, 0, 0, 0};
        double[][] Deslocamento = new double[passo][2];

        RealMatrix AMatrix = new Array2DRowRealMatrix(A);
        RealMatrix BMatrix = new Array2DRowRealMatrix(B);
        RealMatrix CMatrix = new Array2DRowRealMatrix(C);

        for (int i = 0; i < passo; i++) {
            RealMatrix xMatrix = new Array2DRowRealMatrix(x);
            RealMatrix OscilaçãoMatrix = new Array2DRowRealMatrix(new double[]{Oscilação[i]});

            RealMatrix k1 = CalcularDerivada(AMatrix, BMatrix, xMatrix, OscilaçãoMatrix);
            RealMatrix k2 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k1.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k3 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k2.scalarMultiply(dt / 2)), OscilaçãoMatrix);
            RealMatrix k4 = CalcularDerivada(AMatrix, BMatrix, xMatrix.add(k3.scalarMultiply(dt)), OscilaçãoMatrix);

            RealMatrix dx = k1.add(k2.scalarMultiply(2)).add(k3.scalarMultiply(2)).add(k4).scalarMultiply(dt / 6);

            x = xMatrix.add(dx).getColumn(0);
            
            System.out.println("b");
            for (int c = 0; c < x.length; c++) {
                System.out.println(x[c]); 
            System.out.println("a");// Imprime cada elemento do array
            }
            
            RealMatrix DeslocamentoMatrix = CMatrix.multiply(new Array2DRowRealMatrix(x));

            Deslocamento[i][0] = DeslocamentoMatrix.getEntry(0, 0);
            setINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(i, Deslocamento[i][0]);
            MassaSuspensa.setPosição(Deslocamento[i][0]);
            
            Deslocamento[i][1] = DeslocamentoMatrix.getEntry(1, 0);
            setINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(i, Deslocamento[i][1]);
            MassaNaoSuspensa.setPosição(Deslocamento[i][1]);
            
            setINF_SISTEMA_SUSPENSAO_Diferença_Desl(i, Deslocamento[i][1]-Deslocamento[i][0]);
            
            

            
            double acel = (k_s * (x[2] - x[0]) + c_s * (x[3] - x[2])) / m_s;
            
            setINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(i, acel);
            
            
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
    
    //setters
    public void setINF_SISTEMA_SUSPENSAO_Tempo(int passo, double Tempo) {
    	this.INF_SISTEMA_SUSPENSAO[passo][0] = Tempo;
    }
    public void setINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(int passo, double Desl_Massa_Sus) {
    	this.INF_SISTEMA_SUSPENSAO[passo][1] = Desl_Massa_Sus;
    }
    public void setINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(int passo, double Desl_Massa_N_Sus) {
    	this.INF_SISTEMA_SUSPENSAO[passo][2] = Desl_Massa_N_Sus;
    }
    public void setINF_SISTEMA_SUSPENSAO_Diferença_Desl(int passo, double Diferença_Desl) {
    	this.INF_SISTEMA_SUSPENSAO[passo][3] = Diferença_Desl;
    }
    public void setINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(int passo, double Aceleracao_Sus) {
    	this.INF_SISTEMA_SUSPENSAO[passo][4] = Aceleracao_Sus;
    }
    
    //getters
    public double getINF_SISTEMA_SUSPENSAO_Tempo(int passo) {
        return this.INF_SISTEMA_SUSPENSAO[passo][0];
    }

    public double getINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(int passo) {
        return this.INF_SISTEMA_SUSPENSAO[passo][1];
    }

    public double getINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(int passo) {
        return this.INF_SISTEMA_SUSPENSAO[passo][2];
    }

    public double getINF_SISTEMA_SUSPENSAO_Diferença_Desl(int passo) {
        return this.INF_SISTEMA_SUSPENSAO[passo][3];
    }

    public double getINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(int passo) {
        return this.INF_SISTEMA_SUSPENSAO[passo][4];
    }
    
    /*
    public static void main(String[] args) {
        Amortecedor amortecedor = new Amortecedor();
        amortecedor.setConstanteC(1000);
        Mola molaSuspensao = new Mola();
        molaSuspensao.setConstanteK(15000);
        Mola molaPneu = new Mola();
        molaPneu.setConstanteK(200000);
        Massa massaSuspensa = new Massa();
        massaSuspensa.setMassa(250);
        Massa massaNaoSuspensa = new Massa();
        massaNaoSuspensa.setMassa(5);
        Estrada estrada = new Estrada(0.1);

        SistemaDeSuspensao sis = new SistemaDeSuspensao();
        sis.setParametros(amortecedor, molaSuspensao, molaPneu, massaSuspensa, massaNaoSuspensa, estrada);

        sis.Calcular();

        System.out.printf(" \n%.3f", sis.getDeslocamentoMAX_N_SUS());

        // Altere o limite do laço de 1000 para 999 para garantir que não ultrapasse o índice máximo
        for (int i = 0; i < 1000; i++) {
            System.out.printf("\nTempo: %.3f", sis.getINF_SISTEMA_SUSPENSAO_Tempo(i));
           // System.out.printf("***%.3f", sis.getINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(i));
            //System.out.printf("***%.3f", sis.getINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(i));
            //System.out.printf("***%.3f", sis.getINF_SISTEMA_SUSPENSAO_Diferença_Desl(i));
            System.out.printf(" Aceleração: %.3f", sis.getINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(i));
        }
    }
    */
}