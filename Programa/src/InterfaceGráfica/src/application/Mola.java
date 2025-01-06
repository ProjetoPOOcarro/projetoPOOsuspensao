package application;
/*
 * - A classe mola deriva da classe peça
 * - Representa um guardo de energia
 * - ConstanteK é a constante de elasticidade da mola
 */
public class Mola extends Peça{
	
	//------Atributos------
	private double ConstanteK;
	
	//------Metodos------
	//Setter
    public void setConstanteK(double ConstanteK) {
        this.ConstanteK = ConstanteK;
    }
    
	//Getter
    public double getConstanteK() {
        return ConstanteK;
    }

}
