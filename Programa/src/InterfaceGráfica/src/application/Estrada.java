package application;

public class Estrada {

	//Atributos
	double Amplitude;
	
	//Construtor
    public Estrada(double Amplitude) {
        this.Amplitude = Amplitude;
    }
	
    //Função que calcula a oscilação
	double OscilacaoEstrada(double tempo){
		double altura = Amplitude * Math.sin(2 * tempo * Math.PI); 
		return altura;
	}
	
	//Getter
    public double getAmplitude() {
        return Amplitude;
    }
	
}
