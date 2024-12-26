package InterfaceGráfica.src.application;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class StateSpaceSystem {
    private RealMatrix A, B, C, D; // Matrizes do sistema
    private RealMatrix x; // Estado atual

    // Construtor
    public StateSpaceSystem(double[][] A, double[][] B, double[][] C, double[][] D, double[][] x0) {
        this.A = MatrixUtils.createRealMatrix(A);
        this.B = MatrixUtils.createRealMatrix(B);
        this.C = MatrixUtils.createRealMatrix(C);
        this.D = MatrixUtils.createRealMatrix(D);
        this.x = MatrixUtils.createRealMatrix(x0); // Estado inicial
    }

    // Atualizar o estado e calcular a saída
    public double[][] step(double[][] u) {
        RealMatrix U = MatrixUtils.createRealMatrix(u);

        // x(k+1) = A*x(k) + B*u(k)
        x = A.multiply(x).add(B.multiply(U));

        // y(k) = C*x(k) + D*u(k)
        RealMatrix y = C.multiply(x).add(D.multiply(U));

        return y.getData(); // Retorna a saída
    }

    // Getter para obter o estado atual
    public double[][] getState() {
        return x.getData();
    }
}
