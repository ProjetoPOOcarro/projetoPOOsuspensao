package InterfaceGráfica.src.application;

//Representa uma dissipação de energia
public class Amortecedor extends Peça{

	//Atributos
	double ConstanteC;
	
	//Construtor
    public Amortecedor(double ConstanteC) {
        this.ConstanteC = ConstanteC;
    }
    
	//Getter
    public double getConstanteC() {
        return ConstanteC;
    }
	
}
