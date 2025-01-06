package application;
/*
 * - Classe feita para simular uma estrada na qual o sistema de suspensão irá andar
 * - Possui uma oscilação senoidal para seguir as normas tecnicas, mas poderia ter outros perfis
 */
public class Estrada {

	//------Atributos------
	private double Amplitude;
	
	//------Construtor------
    public Estrada(double Amplitude) {
        this.Amplitude = Amplitude;
    }
    
	//------Metodos------
    
    //Função que calcula a oscilação da estrada
    //Retorna a altura da estrada (pode ser negativa já que o ponto de referencia esta no meio da função)
	double OscilacaoEstrada(double tempo){
		double altura = Amplitude * Math.sin(2 * tempo * Math.PI); 
		return altura;
	}
	
	//Getter
    public double getAmplitude() {
        return Amplitude;
    }
	
}
