package InterfaceGráfica.src.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.commons.math3.linear.*;

public class Teste extends Application {

    private double m_s = 250; // Massa suspensa (kg)
    private double m_u = 50;  // Massa não suspensa (kg)
    private double k_s = 15000; // Rigidez da suspensão (N/m)
    private double k_t = 200000; // Rigidez do pneu (N/m)
    private double c_s = 1000; // Amortecimento da suspensão (Ns/m)

    private RealMatrix A;
    private RealMatrix B;
    private RealMatrix C;
    private RealMatrix D;

    public Teste() {
        A = new Array2DRowRealMatrix(new double[][]{
            {0, 1, 0, 0},
            {-k_s / m_s, -c_s / m_s, k_s / m_s, c_s / m_s},
            {0, 0, 0, 1},
            {k_s / m_u, c_s / m_u, -(k_s + k_t) / m_u, -c_s / m_u}
        });

        B = new Array2DRowRealMatrix(new double[][]{{0}, {0}, {0}, {k_t / m_u}});
        C = new Array2DRowRealMatrix(new double[][]{{1, 0, 0, 0}, {0, 0, 1, 0}});
        D = new Array2DRowRealMatrix(new double[][]{{0}, {0}});
    }

    @Override
    public void start(Stage primaryStage) {
        // Eixos X e Y para o gráfico
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tempo");
        yAxis.setLabel("Posição");

        // Criando o gráfico
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Movimento do Sistema");

        // Adicionando dados à série (apenas um exemplo de dados)
        for (double i = 0; i < 200; i = i + 0.1) {
            series.getData().add(new XYChart.Data<>(i, Math.sin(i * 0.1)));
        }

        lineChart.getData().add(series);

        // Configurando a cena
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setTitle("Simulação do Sistema");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

