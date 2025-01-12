package application;
/*
 * - Classe simula um sistema de suspensão com massas, molas e um amortecedor percorrendo uma estrada
 * - Foi adicionada a biblioteca APACHE para ajudar a resolver os cálculos deste programa
 */
import org.apache.commons.math3.linear.*;

public class SistemaDeSuspensao {

	//------Atributos------
	
	//Peças do sistema de suspensão
    Amortecedor Amortecedor;
    Mola MolaSuspensao;
    Mola MolaPneu;
    Massa MassaSuspensa;
    Massa MassaNaoSuspensa;
    Estrada Estrada;
    
    //Atributos de saída do sistema
    private double DeslocamentoMAX_SUS = 0;
    private double DeslocamentoMAX_N_SUS = 0;
    private double PICO_ACELERACAO = 0;
    private double RMS_ACELERACAO;
    
    //Atributos que ajudam nos cálculos
    public double soma_acel;
    public double dt = 0.01;//Passo de tempo (s)
    public int passo = 1000;//Número de passos
    //passo * dt = tempo total de simulação (neste caso 10s)
    //Esses atributos podem ser mudados para melhorar a precisão da simulação
    
    //Vetor que armazena saídas do programa junto com cada passo
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
    }
    
	//------Metodos------
    //Função faz a simulação e calcula todos os parametros de saída
    public void Calcular() {
    	
    	//Variaveis que serão usadas nos calculos do programa
    	//Matrizes A, B e C estão mais detalhadas nos DOCS, mas basicamente são organizadas de uma forma a ajudar nos calculos do sistema
        double[][] A = {
                {0, 1, 0, 0},
                {-MolaSuspensao.getConstanteK() / MassaSuspensa.getMassa(), -Amortecedor.getConstanteC() / MassaSuspensa.getMassa(), MolaSuspensao.getConstanteK() / MassaSuspensa.getMassa(), Amortecedor.getConstanteC() / MassaSuspensa.getMassa()},
                {0, 0, 0, 1},
                {MolaSuspensao.getConstanteK() / MassaNaoSuspensa.getMassa(), Amortecedor.getConstanteC() / MassaNaoSuspensa.getMassa(), -(MolaSuspensao.getConstanteK() + MolaPneu.getConstanteK()) / MassaNaoSuspensa.getMassa(), -Amortecedor.getConstanteC() / MassaNaoSuspensa.getMassa()}
        };
        double[][] B = {
                {0},
                {0},
                {0},
                {MolaPneu.getConstanteK() / MassaNaoSuspensa.getMassa()}
        };
        double[][] C = {
                {1, 0, 0, 0},
                {0, 0, 1, 0}
        };
        
        //Matriz que guarda o tempo a cada passo
        double[] Tempo = new double[passo];
        
        //Matriz que guarda a oscilação da estrada a cada passo
        double[] Oscilação = new double[passo];
        
        //Matriz que guarda várias informações a respeito das massas suspensa e não suspensa  a cada passo
        double[] x = {0, 0, 0, 0};
        /*
         * x[0] = Deslocamento da massa suspensa
         * x[1] = Velocidade da massa suspensa
         * x[2] = Deslocamento da massa não suspensa
         * x[3] = Velocidade da massa não suspensa
         */
        
        //Matriz que guarda o deslocamento das massa a cada passo
        double[][] Deslocamento = new double[passo][2];
        
        //Guarda as informações de tempo e oscilação nos vetores
        for (int i = 0; i < passo; i++) {
        	INF_SISTEMA_SUSPENSAO [i][0] = i * dt;
        	Tempo[i] = i * dt;
            Oscilação[i] = Estrada.OscilacaoEstrada(Tempo[i]);
        }

        //Cria as mesmas matrizes para serem usadas com a biblioteca APACHE
        RealMatrix MatrizA = new Array2DRowRealMatrix(A);
        RealMatrix MatrizB = new Array2DRowRealMatrix(B);
        RealMatrix MatrizC = new Array2DRowRealMatrix(C);

        for (int i = 0; i < passo; i++) {
        	
            //Cria as mesmas matrizes para serem usadas com a biblioteca APACHE
            RealMatrix xMatriz = new Array2DRowRealMatrix(x);
            RealMatrix OscilaçãoMatriz = new Array2DRowRealMatrix(new double[]{Oscilação[i]});

            //k1, k2, k3, k4 são números do metodo de Runge-Kutta
            RealMatrix k1 = CalcularDerivada(MatrizA, MatrizB, xMatriz, OscilaçãoMatriz);
            RealMatrix k2 = CalcularDerivada(MatrizA, MatrizB, xMatriz.add(k1.scalarMultiply(dt / 2)), OscilaçãoMatriz);
            RealMatrix k3 = CalcularDerivada(MatrizA, MatrizB, xMatriz.add(k2.scalarMultiply(dt / 2)), OscilaçãoMatriz);
            RealMatrix k4 = CalcularDerivada(MatrizA, MatrizB, xMatriz.add(k3.scalarMultiply(dt)), OscilaçãoMatriz);

            //Calculo do dx a partir da equação de Runge-Kutta
            RealMatrix dx = k1.add(k2.scalarMultiply(2)).add(k3.scalarMultiply(2)).add(k4).scalarMultiply(dt / 6);

            //dx é adicionado ao x
            x = xMatriz.add(dx).getColumn(0);
           
            //Deslocamento é calculado a partir do novo x
            RealMatrix DeslocamentoMatriz = MatrizC.multiply(new Array2DRowRealMatrix(x));

            //Vetor deslocamento guarda o deslocamento da massa suspensa calculado no passo i
            Deslocamento[i][0] = DeslocamentoMatriz.getEntry(0, 0);
            //Guarda o deslocamento da massa suspensa calculado no passo i
            setINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(i, Deslocamento[i][0]);
            
            //Vetor deslocamento guarda o deslocamento da massa não suspensa calculado no passo i
            Deslocamento[i][1] = DeslocamentoMatriz.getEntry(1, 0);
            //Guarda o deslocamento da massa não suspensa calculado no passo i
            setINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(i, Deslocamento[i][1]);
            
            //Calcula a diferença de deslocamento e guarda a cada passo i
            setINF_SISTEMA_SUSPENSAO_Diferença_Desl(i, Deslocamento[i][1]-Deslocamento[i][0]);

            //Variavel que calcula a aceleração da massa suspensa
            double acel = (MolaSuspensao.getConstanteK() * (Deslocamento[i][1] - Deslocamento[i][0]) + Amortecedor.getConstanteC() * (x[3] - x[1])) / MassaSuspensa.getMassa();
            //Guarda a aceleração da massa suspensa a cada passo i
            setINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(i, acel);
            
            //Pega o deslocamento maximo da massa suspensa durante a simulação
            if (getDeslocamentoMAX_SUS() < Deslocamento[i][0]) {
            	setDeslocamentoMAX_SUS(Deslocamento[i][0]);
            }
            //Pega o deslocamento maximo da massa não suspensa durante a simulação
            if (getDeslocamentoMAX_N_SUS() < Deslocamento[i][1]) {
            	setDeslocamentoMAX_N_SUS(Deslocamento[i][1]);
            }
            //Calcula o pico de aceleração durante a simulação
            if (getPICO_ACELERACAO() < acel) {
            	setPICO_ACELERACAO(acel);
            }
            
            //Calcula a soma das acelerações
            soma_acel = soma_acel +  Math.pow(Math.abs(acel), 2);
            
            //Se for a última volta, calcula o RMS
            if (i == (passo - 1)) {
            	 System.out.printf("a%f\n", soma_acel);
            	setRMS_ACELERACAO(Math.sqrt(soma_acel/passo));
            }
        }
    }

    //Função que calcula a derivada de x
    private static RealMatrix CalcularDerivada(RealMatrix A, RealMatrix B, RealMatrix x, RealMatrix Oscilação) {
        return A.multiply(x).add(B.multiply(Oscilação));
    }
    
    //------Getters e Setters------
    
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
    //Getter do Pico de aceleração
    public double getPICO_ACELERACAO() {
        return PICO_ACELERACAO;
    }
    //Setter do Pico de aceleração
    public void setPICO_ACELERACAO(double PICO_ACELERACAO) {
        this.PICO_ACELERACAO = PICO_ACELERACAO;
    }
    //Getter do RMS da aceleração
    public double getRMS_ACELERACAO() {
        return RMS_ACELERACAO;
    }
    //Setter do RMS da aceleração
    public void setRMS_ACELERACAO(double RMS_ACELERACAO) {
        this.RMS_ACELERACAO = RMS_ACELERACAO;
    }

    //setters das informações de saída do sistema de suspensão
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
    
    //getters das informações de saída do sistema de suspensão
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
}