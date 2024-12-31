package application;

//Representa uma guardo de energia
public class Mola extends Peça{
	
	//Atributos
	private double ConstanteK;
	
	//Setter
    public void setConstanteK(double ConstanteK) {
        this.ConstanteK = ConstanteK;
    }
    
	//Getter
    public double getConstanteK() {
        return ConstanteK;
    }

}
