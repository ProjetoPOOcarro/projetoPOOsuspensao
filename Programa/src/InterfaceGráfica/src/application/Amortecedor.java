package application;
/*
 * - A classe amortecedor deriva da classe peça
 * - Representa uma dissipição de energia
 * - ConstanteC é a constante de amortecimento
 */
public class Amortecedor extends Peça{

	//------Atributos------
	private double ConstanteC;
	
	//------Metodos------
	//Setter
    public void setConstanteC(double ConstanteC) {
        this.ConstanteC = ConstanteC;
    }
    
	//Getter
    public double getConstanteC() {
        return ConstanteC;
    }
	
}
