package grafico;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grafico {
    private JFrame janela;
    private JPanel painel;
    private int ondaX = 0; // Posição inicial das ondas (à esquerda)
    private int amplitudeOnda = 20; // Amplitude inicial da onda
    private int velocidadeOnda = 1; // Velocidade inicial da onda
    private int frequenciaOnda = 150; // Frequência inicial (distância entre as ondas)
    private int posicaoYBola; // Posição Y da bola
    private final int raioBola = 35; // Raio da bola
    private final int alturaPainel = 600; // Altura do painel (ajustada para o teste)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Grafico janela = new Grafico();
                    janela.janela.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Construtor que configura a janelaa
    public Grafico() {
        janela = new JFrame();
        janela.setBounds(100, 100, 800, 600);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout());

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharFundo(g);
                desenharOndas(g);
                desenharBola(g);
            }
        };
        janela.getContentPane().add(painel, BorderLayout.CENTER);

        // Inicializa a posição da bola com base na onda
        posicaoYBola = alturaPainel / 2 - raioBola;

        // Painel para os sliders
        JPanel painelControles = new JPanel();
        painelControles.setLayout(new GridLayout(3, 1));

        // Slider para controlar a velocidade
        JSlider sliderVelocidade = new JSlider(1, 5, velocidadeOnda);
        sliderVelocidade.setMajorTickSpacing(1);
        sliderVelocidade.setPaintTicks(true);
        sliderVelocidade.setPaintLabels(true);
        sliderVelocidade.addChangeListener(e -> velocidadeOnda = sliderVelocidade.getValue());

        // Slider para controlar a amplitude
        JSlider sliderAmplitude = new JSlider(1, 40, amplitudeOnda);
        sliderAmplitude.setMajorTickSpacing(10);
        sliderAmplitude.setMinorTickSpacing(5);
        sliderAmplitude.setPaintTicks(true);
        sliderAmplitude.setPaintLabels(true);
        sliderAmplitude.addChangeListener(e -> amplitudeOnda = sliderAmplitude.getValue());

        // Slider para controlar a distância entre as ondas (frequência)
        JSlider sliderFrequencia = new JSlider(150, 300, frequenciaOnda);
        sliderFrequencia.setMajorTickSpacing(50);
        sliderFrequencia.setMinorTickSpacing(10);
        sliderFrequencia.setPaintTicks(true);
        sliderFrequencia.setPaintLabels(true);
        sliderFrequencia.addChangeListener(e -> frequenciaOnda = sliderFrequencia.getValue());

        painelControles.add(new JLabel("Velocidade da Onda (1 a 5):"));
        painelControles.add(sliderVelocidade);
        painelControles.add(new JLabel("Amplitude da Onda (1 a 40):"));
        painelControles.add(sliderAmplitude);
        painelControles.add(new JLabel("Distância entre as Ondas (150 a 300):"));
        painelControles.add(sliderFrequencia);

        janela.getContentPane().add(painelControles, BorderLayout.SOUTH);

        // Cria o timer para animar as ondas
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ondaX += velocidadeOnda; // Move a onda para a direita
                if (ondaX > painel.getWidth()) {
                    ondaX = -painel.getWidth();
                }

                // Atualiza a posição da bola para seguir a onda
                atualizarPosicaoBola();

                painel.repaint();
            }
        });
        timer.start();
    }

    // Método para desenhar o fundo preto na parte inferior
    private void desenharFundo(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, alturaPainel / 2, painel.getWidth(), alturaPainel / 2); // Faixa preta abaixo das ondas
    }

    // Método para desenhar ondas suaves (senoides)
    private void desenharOndas(Graphics g) {
        g.setColor(Color.YELLOW);
        int larguraPainel = painel.getWidth();

        for (int i = 0; i < larguraPainel; i++) {
            int y = (int) (amplitudeOnda * Math.sin((i + ondaX) * 2 * Math.PI / frequenciaOnda) + alturaPainel / 2);
            if (y < alturaPainel / 2) {
                g.drawLine(i, alturaPainel / 2, i, y);
            }
        }
    }

    // Método para desenhar o círculo que oscila com a onda
    private void desenharBola(Graphics g) {
        g.setColor(Color.GRAY);
        int larguraPainel = painel.getWidth();

        // Posição fixa horizontal no meio da tela
        int bolaX = larguraPainel / 2;

        // Desenha a bola na posição Y determinada pela onda
        int ondaYNoCentro = (int) (amplitudeOnda * Math.sin((bolaX + ondaX) * 2 * Math.PI / frequenciaOnda) + alturaPainel / 2);

        // Ajusta a posição da bola de acordo com a onda
        int bolaY = posicaoYBola;

        // A base da bola não pode ultrapassar a faixa preta, então ajustamos a posição
        if (bolaY + raioBola * 2 > alturaPainel / 2) {
            bolaY = alturaPainel / 2 - raioBola * 2;
        }

        // Agora desenha o círculo ajustado
        g.fillOval(bolaX - raioBola, bolaY, 2 * raioBola, 2 * raioBola);
    }

    // Atualiza a posição da bola para que ela suba ou desça conforme a onda
    private void atualizarPosicaoBola() {
        int larguraPainel = painel.getWidth();
        int alturaPainel = painel.getHeight();

        // Posição do círculo no meio da tela
        int bolaX = larguraPainel / 2;

        // Calcula a altura da onda no centro da bola (no X da bola)
        int ondaYNoCentro = (int) (amplitudeOnda * Math.sin((bolaX + ondaX) * 2 * Math.PI / frequenciaOnda) + alturaPainel / 2);

        // Ajusta a posição Y da bola para que ela acompanhe a onda de forma mais controlada
        if (posicaoYBola > ondaYNoCentro) {
            posicaoYBola -= 1;  // A bola sobe
        } else if (posicaoYBola < ondaYNoCentro) {
            posicaoYBola += 1;  // A bola desce até a posição da onda
        }
    }
}