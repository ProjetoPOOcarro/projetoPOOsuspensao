package application;

//Representa uma guardo de energia
public class Mola extends Peça{
	
	//Atributos
	double ConstanteK;
	
	//Construtor
    public Mola(double ConstanteK) {
        this.ConstanteK = ConstanteK;
    }
    
	//Getter
    public double getConstanteK() {
        return ConstanteK;
    }

}
