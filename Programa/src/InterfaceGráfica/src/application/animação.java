package application;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

public class animação {
	public Scene criarAnimação() {
		Circle roda1 = new Circle(50, Color.BLACK);
		roda1.setRadius(10);//deslocamento da suspensão aceleração da massa suspensa
		roda1.setCenterX(-40);
		roda1.setCenterY(300);
        
		Circle roda2 = new Circle(50, Color.BLACK);
		roda2.setRadius(10);
		roda2.setCenterX(-10);
		roda2.setCenterY(300);
        
		Rectangle carro = new Rectangle(50, 20, Color.RED);//largura,altura,cor
		carro.setY(277); // Posição inicial no eixo Y
		carro.setX(-50); // Posição inicial no eixo x
	     
		Rectangle cabine = new Rectangle(20, 20, Color.RED);
		cabine.setY(262); // Posição inicial no eixo Y
		cabine.setX(-30); // Posição inicial no eixo x
	    
		Rectangle farol = new Rectangle(7, 7, Color.YELLOW);
		farol.setY(279); // Posição inicial no eixo Y
		farol.setX(-7); // Posição inicial no eixo x
	    
		Rectangle paraBrisa = new Rectangle(10, 12, Color.DARKBLUE);
		paraBrisa.setY(265); // Posição inicial no eixo Y
		paraBrisa.setX(-20); // Posição inicial no eixo x
		
		 Image imagemFundo = new Image("file:images.jpg");		
		ImageView imageViewFundo = new ImageView(imagemFundo);

        // Definir o tamanho da imagem para preencher o Painel
        imageViewFundo.setFitWidth(580); // Largura
        imageViewFundo.setFitHeight(320); // Altura
        imageViewFundo.setPreserveRatio(false); // Manter a proporção da imagem
      
		Pane grupo1 = new Pane(cabine,carro,farol,paraBrisa);
		Pane grupo2 = new Pane(roda1,roda2);
		Pane painel = new Pane();
		painel.getChildren().addAll(imageViewFundo,grupo1,grupo2);
			
	     // Configurando a animação em x da massa suspensa
	     TranslateTransition translateX = new TranslateTransition();
	     translateX.setNode(grupo1);
	     translateX.setDuration(Duration.seconds(4)); // Duração de 4 segundos
	     translateX.setByX(640); // Movimentar 6400 unidades no eixo X
	     translateX.setCycleCount(TranslateTransition.INDEFINITE); // Repetir indefinidamente
	     translateX.setAutoReverse(false); // Reverter automaticamente
	     translateX.play();
	     
	  // Configurando a animação em Y da massa suspensa
	     TranslateTransition translateY = new TranslateTransition();
	     translateY.setNode(grupo1);
	     translateY.setDuration(Duration.seconds(1)); // Duração de 4 segundos
	     translateY.setByY(-5); // Movimentar 6400 unidades no eixo X
	     translateY.setCycleCount(TranslateTransition.INDEFINITE); // Repetir indefinidamente
	     translateY.setAutoReverse(true); // Reverter automaticamente
	     translateY.play();
	     
	     
	     TranslateTransition translate2 = new TranslateTransition();
	     translate2.setNode(grupo2);
	     translate2.setDuration(Duration.seconds(4)); 
	     translate2.setByX(640); 
	     translate2.setCycleCount(TranslateTransition.INDEFINITE); 
	     translate2.setAutoReverse(false); 
	     translate2.play();
        
	     return new Scene(painel, 580, 320);
		}
	}
