/*
 * essa classe serve para criar uma animação de um carro que sofre os efeitos calculados do comportamentos
 *do amortecedor de forma simulada 
 */
package application;

import javafx.scene.image.Image;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class animação {
	
	 public Scene criarAnimação(double Vmax,double Vmin) {
        Circle roda1 = new Circle(50, Color.BLACK);
        roda1.setRadius(10); 
        roda1.setCenterX(-40);//posição incial em x da roda fora da tela
        roda1.setCenterY(300);

        Circle roda2 = new Circle(50, Color.BLACK);
        roda2.setRadius(10);
        roda2.setCenterX(-10);//posição incial em x da roda fora da tela
        roda2.setCenterY(300);

        Rectangle carro = new Rectangle(50, 20, Color.RED); // Largura, altura, cor
        carro.setY(277); // Posição inicial no eixo Y
        carro.setX(-50); // Posição inicial no eixo X

        Rectangle cabine = new Rectangle(20, 20, Color.RED);
        cabine.setY(262); // Posição inicial no eixo Y
        cabine.setX(-30); // Posição inicial no eixo X

        Rectangle farol = new Rectangle(7, 7, Color.YELLOW);
        farol.setY(279); // Posição inicial no eixo Y
        farol.setX(-7); // Posição inicial no eixo X

        Rectangle paraBrisa = new Rectangle(10, 12, Color.DARKBLUE);
        paraBrisa.setY(265); // Posição inicial no eixo Y
        paraBrisa.setX(-20); // Posição inicial no eixo X

        Image imagemFundo = new Image("file:images.jpg");
        ImageView imageViewFundo = new ImageView(imagemFundo);

        // Definir o tamanho da imagem para preencher o Painel
        imageViewFundo.setFitWidth(580); // Largura
        imageViewFundo.setFitHeight(320); // Altura
        imageViewFundo.setPreserveRatio(false); // Manter a proporção da imagem

        Pane grupo1 = new Pane(cabine, carro, farol, paraBrisa);
        Pane grupo2 = new Pane(roda1, roda2);
        //separação dos elementos animados em grupos para animar juntos ao invés de animar um por um 
        Pane painel = new Pane();
        painel.getChildren().addAll(imageViewFundo, grupo1, grupo2);//junta tudo em um grupo só para mostrar na tela

        // Configurando a animação em X da massa suspensa
        TranslateTransition translateX = new TranslateTransition();
        translateX.setNode(grupo1);
        translateX.setDuration(Duration.seconds(10)); // Duração de 4 segundos
        translateX.setByX(640); // Movimentar 640 unidades no eixo X
        translateX.setCycleCount(TranslateTransition.INDEFINITE); // Repetir indefinidamente
        translateX.setAutoReverse(false); // Reverter automaticamente
        translateX.play();

        // Configurando a animação em Y da massa suspensa
        TranslateTransition translateY = new TranslateTransition();
        translateY.setNode(grupo1);
        translateY.setDuration(Duration.seconds(1)); // Duração de 1 segundo
        translateY.setByY(0);
        translateY.setCycleCount(1); // não repetir indefinidamente
        translateY.setAutoReverse(false); // não reverter automaticamente
        translateY.play();
        
        TranslateTransition translateY2 = new TranslateTransition();
        translateY2.setNode(grupo1);
        translateY2.setDuration(Duration.seconds(2)); // Duração de 1 segundo
        translateY2.setFromY(Vmax*15);
        translateY2.setToY(-Vmin*15);
        translateY2.setCycleCount(TranslateTransition.INDEFINITE); // Repetir indefinidamente
        translateY2.setAutoReverse(true); // Reverter automaticamente
        translateY2.play();
        
        TranslateTransition translate2 = new TranslateTransition();
        translate2.setNode(grupo2);
        translate2.setDuration(Duration.seconds(10));
        translate2.setByX(640);
        translate2.setCycleCount(TranslateTransition.INDEFINITE);
        translate2.setAutoReverse(false);
        translate2.play();
        
        Text Conforto;
        SistemaDeSuspensao PicoDeAceleração = new SistemaDeSuspensao();
        double AceleraçãoMax = PicoDeAceleração.getPICO_ACELERACAO();
        if(AceleraçãoMax<=0.5) {
        	Conforto = new Text(10, 20, "Faixa idela Para conforto");
        	Conforto.setFill(Color.BLACK);
        }
        else {
        	Conforto = new Text(10, 20, "Faixa não idela Para conforto");
        	Conforto.setFill(Color.BLACK);
        }//diz se o resultado da aceleração é o ideal ou não
        painel.getChildren().add(Conforto);

        Scene cena = new Scene(painel, 580, 320);
        return cena;
    }
}
