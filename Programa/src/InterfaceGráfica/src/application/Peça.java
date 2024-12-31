package application;

//Classe abstrata que servirá de base para outras
public class Peça {

	//Atributos
	private String Nome;
	private double Massa;
	
	//Setter
    public void setNome(String Nome) {
        this.Nome = Nome;
    }
    
    public void setMassa(double Massa) {
        this.Massa = Massa;
    }
    
    //Getter
    public double getMassa() {
        return Massa;
    }
    
    public String getNome() {
        return Nome;
    }
    
	
}
