package application;

public class Veiculo {
	
	// Atributos
	private String Nome;
	private SistemaDeSuspensao sistemadesuspensao;
	
	//Setter
    public void setNome(String Nome) {
        this.Nome = Nome;
    }
    public void setsistemadesuspensao(SistemaDeSuspensao sistemadesuspensao) {
        this.sistemadesuspensao = sistemadesuspensao;
    }

	//Getter
    public String getNome() {
        return Nome;
    }
    public SistemaDeSuspensao getsistemadesuspensao() {
        return sistemadesuspensao;
    }
	
	
	
	
}
