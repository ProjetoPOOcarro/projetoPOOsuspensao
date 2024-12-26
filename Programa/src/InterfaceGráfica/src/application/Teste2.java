package InterfaceGráfica.src.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Teste2 extends Application {

    @Override
    public void start(Stage stage) {
        // Definir os eixos do gráfico
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Passo");
        yAxis.setLabel("Saída (y)");

        // Criar o gráfico
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Simulação do Sistema de Espaço de Estados");

        // Série de dados para a saída (y)
        XYChart.Series<Number, Number> serie = new XYChart.Series<>();
        serie.setName("Saída (y)");

        // Matrizes do sistema
        double[][] A = {
            {0, 1},
            {-2, -3}
        };

        double[][] B = {
            {0},
            {1}
        };

        double[][] C = {
            {1, 0}
        };

        double[][] D = {
            {0}
        };

        // Estado inicial
        double[][] x0 = {
            {0},  // Posição inicial
            {0}   // Velocidade inicial
        };

        // Criar o sistema de espaço de estados
        EstadoEspaco sistema = new EstadoEspaco(A, B, C, D, x0);

        // Simular o sistema por 10 passos e coletar os dados para o gráfico
        for (double i = 0; i < 4; i = i + 0.1) {
        	double[][] u = {{Math.sin(i*Math.PI*2)}};
            double[][] y = sistema.passo(u); // Calcula a saída
            serie.getData().add(new XYChart.Data<>(i, y[0][0]));  // Adiciona o valor de saída no gráfico
        }

        // Adicionar a série de dados ao gráfico
        lineChart.getData().add(serie);

        // Configurar a cena
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setTitle("Simulação do Sistema de Espaço de Estados");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
