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
    private double DeslocamentoDaMassaSuspensa;
    private double DeslocamentoDaMassaNãoSuspensa;
    private double DeslocamentoDaSuspensão;
    private double AceleraçãoDaSuspensão;
    private double tempo1; // Variável para controlar o tempo no eixo X
    private double tempo2; // Variável para controlar o tempo no eixo X
    private double tempo3; // Variável para controlar o tempo no eixo X
    private int aux;
    private int aux2;
    private int aux3;

    public Scene CriarGráfico1(SistemaDeSuspensao sistema) {
        NumberAxis eixoX = new NumberAxis();
        NumberAxis eixoY1 = new NumberAxis();
        eixoX.setLabel("Tempo(s)");
        eixoY1.setLabel("Deslocamento das massas do sistema(m)");

        // Criando o gráfico de linha
        LineChart<Number, Number> graficoDeLinha = new LineChart<>(eixoX, eixoY1);
        graficoDeLinha.setTitle("Gráfico 1");

     // Criando a primeira série de dados (Deslocamento da massa suspensa)
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Deslocamento da Massa Suspensa");

        // Criando a segunda série de dados (tempo / 2)
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Deslocamento da Massa não suspensa");
    	
        // Adicionando a série ao gráfico
        graficoDeLinha.getData().addAll(series1,series2);
     
        // Usar um Timeline para atualizar o gráfico em tempo real
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            // Atualizar o gráfico com o novo valor de DADOS
        	setDeslocamentoDaMassaSuspensa(sistema);
        	setDeslocamentoDaMassaNãoSuspensa(sistema);
        	setTempo1(sistema);
            Platform.runLater(() -> {
                series1.getData().add(new XYChart.Data<>(tempo1, DeslocamentoDaMassaSuspensa));
                series2.getData().add(new XYChart.Data<>(tempo1, DeslocamentoDaMassaNãoSuspensa));
            });
            aux++;
        }));

        timeline.setCycleCount(500); // Configurar para durar 10 segundos (20 atualizações de 0.5 segundos)
        timeline.play();

        // Adicionar o texto e o gráfico ao painel
        Pane painel = new Pane();
        painel.getChildren().addAll(graficoDeLinha);
        Scene cena = new Scene(painel, 580, 400);
        return cena;
    }
    
    public Scene CriarGráfico2(SistemaDeSuspensao sistema) {
        NumberAxis eixoX = new NumberAxis();
        NumberAxis eixoY1 = new NumberAxis();
        eixoX.setLabel("Tempo(s)");
        eixoY1.setLabel("Deslocamento da suspensão(m)");

        // Criando o gráfico de linha
        LineChart<Number, Number> graficoDeLinha = new LineChart<>(eixoX, eixoY1);
        graficoDeLinha.setTitle("Gráfico 2");

     // Criando a primeira série de dados (Deslocamento da massa suspensa)
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Deslocamento da suspensão");

        // Adicionando a série ao gráfico
        graficoDeLinha.getData().add(series1);
     
        // Usar um Timeline para atualizar o gráfico em tempo real
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            // Atualizar o gráfico com o novo valor de DADOS
        	setDeslocamentoDaSuspensão(sistema);
        	setTempo2(sistema);
            Platform.runLater(() -> {
                series1.getData().add(new XYChart.Data<>(tempo2, DeslocamentoDaSuspensão));
             });
            aux2++;
        }));

        timeline.setCycleCount(500); // Configurar para durar 10 segundos (20 atualizações de 0.5 segundos)
        timeline.play();

        // Adicionar o texto e o gráfico ao painel
        Pane painel = new Pane();
        painel.getChildren().addAll(graficoDeLinha);
        Scene cena = new Scene(painel, 580, 400);
        return cena;
    }
    public Scene CriarGráfico3(SistemaDeSuspensao sistema) {
        NumberAxis eixoX = new NumberAxis();
        NumberAxis eixoY1 = new NumberAxis();
        eixoX.setLabel("Tempo(s)");
        eixoY1.setLabel("Aceleração da massa suspensa(m)");

        // Criando o gráfico de linha
        LineChart<Number, Number> graficoDeLinha = new LineChart<>(eixoX, eixoY1);
        graficoDeLinha.setTitle("Gráfico 3");

     // Criando a primeira série de dados (Deslocamento da massa suspensa)
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Aceleração da massa suspensa");

        // Adicionando a série ao gráfico
        graficoDeLinha.getData().add(series1);
     
        // Usar um Timeline para atualizar o gráfico em tempo real
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            // Atualizar o gráfico com o novo valor de DADOS
        	setAceleraçãoDaSuspensão(sistema);
        	setTempo3(sistema);
            Platform.runLater(() -> {
                series1.getData().add(new XYChart.Data<>(tempo3, AceleraçãoDaSuspensão));
             });
            aux3++;
        }));

        timeline.setCycleCount(500); // Configurar para durar 10 segundos (20 atualizações de 0.5 segundos)
        timeline.play();

        // Adicionar o texto e o gráfico ao painel
        Pane painel = new Pane();
        painel.getChildren().addAll(graficoDeLinha);
        Scene cena = new Scene(painel, 580, 400);
        return cena;
    }
    
    public void setDeslocamentoDaMassaSuspensa(SistemaDeSuspensao sistema) {
    	DeslocamentoDaMassaSuspensa = sistema.getINF_SISTEMA_SUSPENSAO_Desl_Massa_Sus(aux);
    }
    public void setDeslocamentoDaMassaNãoSuspensa(SistemaDeSuspensao sistema) {
    	DeslocamentoDaMassaNãoSuspensa = sistema.getINF_SISTEMA_SUSPENSAO_Desl_Massa_N_Sus(aux);
    }
    public void setDeslocamentoDaSuspensão(SistemaDeSuspensao sistema) {
    	DeslocamentoDaSuspensão = sistema.getINF_SISTEMA_SUSPENSAO_Diferença_Desl(aux2);
    }
    public void setAceleraçãoDaSuspensão(SistemaDeSuspensao sistema) {
    	AceleraçãoDaSuspensão = sistema.getINF_SISTEMA_SUSPENSAO_Aceleracao_Sus(aux3);
    }
    public void setTempo1(SistemaDeSuspensao sistema) {
        tempo1 = sistema.getINF_SISTEMA_SUSPENSAO_Tempo(aux);
    }
    public void setTempo2(SistemaDeSuspensao sistema) {
        tempo2 = sistema.getINF_SISTEMA_SUSPENSAO_Tempo(aux2);
    }
    public void setTempo3(SistemaDeSuspensao sistema) {
        tempo3 = sistema.getINF_SISTEMA_SUSPENSAO_Tempo(aux3);
    }
}
