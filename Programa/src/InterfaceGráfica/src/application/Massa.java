package application;

//Representa apenas uma massa suspensa, carroceria por exemplo
public class Massa extends Peça{

	//Atributos
	double Posição;
	
	//Getter
    public double getPosição() {
        return Posição;
    }
    
	//Setter
    public void setPosição(double Posição) {
        this.Posição = Posição;
    }
}
