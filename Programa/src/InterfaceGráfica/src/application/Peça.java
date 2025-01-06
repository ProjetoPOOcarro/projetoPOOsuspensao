package application;
/*
 * - Classe abstrata que servirá de base para outras peças do sistema(massa, mola e amortecedor)
 */
public class Peça {

	//------Atributos------
	private double Massa;

	//------Metodos------
	//Setter
    public void setMassa(double Massa) {
        this.Massa = Massa;
    }
    //Getter
    public double getMassa() {
        return Massa;
    }
}
