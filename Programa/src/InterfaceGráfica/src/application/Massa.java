package application;
/*
 * - A classe massa deriva da classe peça
 * - Representa uma massa especifica, neste caso será a suspensa (carroceria) e a não suspensa(sistema de suspensão) 
 */
public class Massa extends Peça{

	//------Atributos------
	private double Posição;
	
	//------Metodos------
	//Getter
    public double getPosição() {
        return Posição;
    }
    
	//Setter
    public void setPosição(double Posição) {
        this.Posição = Posição;
    }
}
