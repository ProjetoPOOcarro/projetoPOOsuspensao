package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Gráficos {
    static double DADOS;
    private double tempo = 0; // Variável para controlar o tempo no eixo X

    public Scene CriarGráfico1() {
        NumberAxis eixoX = new NumberAxis();
        NumberAxis eixoY = new NumberAxis();
        eixoX.setLabel("Tempo(s)");
        eixoY.setLabel("Deslocamento da massa suspensa(m)");

        // Criando o gráfico de linha
        LineChart<Number, Number> graficoDeLinha = new LineChart<>(eixoX, eixoY);
        graficoDeLinha.setTitle("Gráfico 1");

        // Criando a série de dados
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Valores da Variável");
    	
        // Adicionando a série ao gráfico
        graficoDeLinha.getData().add(series);
     
        // Usar um Timeline para atualizar o gráfico em tempo real
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            // Atualizar o gráfico com o novo valor de DADOS
            Platform.runLater(() -> {
                series.getData().add(new XYChart.Data<>(tempo, DADOS));
            });

            tempo += 0.5; // Incrementar o tempo para o próximo ponto no eixo X
        }));

        timeline.setCycleCount(20); // Configurar para durar 10 segundos (20 atualizações de 0.5 segundos)
        timeline.play();

        // Adicionar o texto e o gráfico ao painel
        Pane painel = new Pane();
        painel.getChildren().addAll(graficoDeLinha);
        Scene cena = new Scene(painel, 580, 400);
        return cena;
    }

    public static void setDados(double aux) {
        DADOS = (aux*-1.0)/10.0;
    }
}
