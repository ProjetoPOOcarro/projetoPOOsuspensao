package InterfaceGráfica.src.application;

public class EstadoEspaco {

    // Matrizes do sistema
    private double[][] A;
    private double[][] B;
    private double[][] C;
    private double[][] D;

    // Estado do sistema
    private double[][] x;

    // Construtor que inicializa as matrizes e o estado inicial
    public EstadoEspaco(double[][] A, double[][] B, double[][] C, double[][] D, double[][] x0) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.x = x0;
    }

    // Função para calcular a próxima iteração do estado e da saída
    public double[][] passo(double[][] u) {
        // Calcula a nova posição do estado x(t)
        double[][] dx = matrizMultiplicacao(A, x);   // A * x(t)
        double[][] Bu = matrizMultiplicacao(B, u);   // B * u(t)
        
        double[][] xNovo = matrizSoma(dx, Bu);       // x(t+1) = A * x(t) + B * u(t)
        
        // Calcula a saída y(t)
        double[][] y = matrizMultiplicacao(C, xNovo); // y(t) = C * x(t)
        y = matrizSoma(y, matrizMultiplicacao(D, u));  // y(t) += D * u(t)
        
        // Atualiza o estado para a próxima iteração
        this.x = xNovo;
        
        return y; // Retorna a saída y(t)
    }

    // Função para multiplicar matrizes
    private double[][] matrizMultiplicacao(double[][] m1, double[][] m2) {
        int m1Linhas = m1.length;
        int m2Colunas = m2[0].length;
        int m1Colunas = m1[0].length;

        double[][] resultado = new double[m1Linhas][m2Colunas];

        for (int i = 0; i < m1Linhas; i++) {
            for (int j = 0; j < m2Colunas; j++) {
                for (int k = 0; k < m1Colunas; k++) {
                    resultado[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return resultado;
    }

    // Função para somar matrizes
    private double[][] matrizSoma(double[][] m1, double[][] m2) {
        int linhas = m1.length;
        int colunas = m1[0].length;
        double[][] resultado = new double[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                resultado[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return resultado;
    }

    // Função para acessar o estado atual
    public double[][] getEstado() {
        return this.x;
    }
}
