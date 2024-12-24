//Classe criada apenas para fazer testes

package InterfaceGráfica.src.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.math3.linear.*;

public class Teste extends Application {

    private static final double dt = 0.01; // Passo de tempo (s)
    private static final int STEPS = (int) (5 / dt); // Total de passos (5 segundos)
    private double time = 0.0; // Tempo inicial
    private int currentStep = 0; // Contador de passos
    private RealMatrix x; // Estado atual
    private double[][] inputForce; // Força aplicada

    @Override
    public void start(Stage primaryStage) {
        // Parâmetros do sistema massa-mola-amortecedor
        double m = 1.0;   // Massa (kg)
        double k = 20.0;  // Rigidez da mola (N/m)
        double c = 2.0;   // Coeficiente de amortecimento (Ns/m)

        // Matrizes do espaço de estados
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {0, 1},
                {-k / m, -c / m}
        });

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{
                {0},
                {1 / m}
        });

        RealMatrix C = new Array2DRowRealMatrix(new double[][]{
                {1, 0}
        });

        RealMatrix D = new Array2DRowRealMatrix(new double[][]{
                {0}
        });

        // Inicializando o estado e a força aplicada
        x = new Array2DRowRealMatrix(new double[2][1]); // Estado inicial: [posição, velocidade]
        inputForce = new double[STEPS][1];
        for (int i = 0; i < STEPS; i++) {
            inputForce[i][0] = 1.0 * Math.sin(2 * Math.PI * i * dt); // Força senoidal
        }

        // Criando o gráfico
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tempo (s)");
        yAxis.setLabel("Deslocamento (m)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Movimento da Massa-Mola-Amortecedor");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Deslocamento");
        lineChart.getData().add(series);

        // Configurando a animação
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(dt * 1000), event -> {
            if (currentStep < STEPS) {
                // Calcula o próximo estado e saída
                RealMatrix xDot = A.multiply(x).add(B.scalarMultiply(inputForce[currentStep][0]));
                x = x.add(xDot.scalarMultiply(dt));
                double displacement = C.multiply(x).getEntry(0, 0);

                // Adiciona o ponto ao gráfico
                series.getData().add(new XYChart.Data<>(time, displacement));

                // Atualiza o tempo e o contador de passos
                time += dt;
                currentStep++;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Configurando a cena
        StackPane root = new StackPane(lineChart);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Simulação em Tempo Real");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
