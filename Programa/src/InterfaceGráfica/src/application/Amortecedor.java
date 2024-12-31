package application;

//Representa uma dissipação de energia
public class Amortecedor extends Peça{

	//Atributos
	private double ConstanteC;
	
	//Setter
    public void setConstanteC(double ConstanteC) {
        this.ConstanteC = ConstanteC;
    }
    
	//Getter
    public double getConstanteC() {
        return ConstanteC;
    }
	
}
