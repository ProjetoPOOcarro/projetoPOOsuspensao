package application;

import org.apache.commons.math3.linear.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class SistemaDeSuspensao extends Application {
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

        // Parâmetros da simulação
        double A_sin = 0.1; // Amplitude do solavanco
        double dt = 0.01; // Passo de tempo (s)
        int steps = 500; // Número de passos (5 s com passo de 0,01 s)

        // Vetores de tempo e entrada
        double[] t = new double[steps];
        double[] u = new double[steps];
        for (int i = 0; i < steps; i++) {
            t[i] = i * dt;
            u[i] = A_sin * Math.sin(2 * Math.PI * t[i]);
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
            y[i][1] = yMatrix.getEntry(1, 0); // Deslocamento da massa não suspensa
        }

        // Criar o gráfico JavaFX
        createChart(stage, t, y);
    }

    // Função para calcular dx/dt
    private static RealMatrix calculateDx(RealMatrix A, RealMatrix B, RealMatrix x, RealMatrix u) {
        return A.multiply(x).add(B.multiply(u));
    }

    // Função para criar o gráfico JavaFX
    private void createChart(Stage stage, double[] time, double[][] data) {
        // Eixos X e Y
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tempo (s)");
        yAxis.setLabel("Deslocamento (m)");

        // Criar gráfico de linha
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Deslocamento das Massas em Função do Tempo");

        // Criar séries de dados
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Massa Suspensa (xs)");

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Massa Não Suspensa (xu)");

        // Preenchendo as séries com os dados da simulação
        for (int i = 0; i < time.length; i++) {
            series1.getData().add(new XYChart.Data<>(time[i], data[i][0]));
            series2.getData().add(new XYChart.Data<>(time[i], data[i][1]));
        }

        // Adicionar as séries ao gráfico
        lineChart.getData().addAll(series1, series2);

        // Configurar a cena e o palco
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setTitle("Simulação de Sistema de Suspensão");
        stage.setScene(scene);
        stage.show();
    }
}

