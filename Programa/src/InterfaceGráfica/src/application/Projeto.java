package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Projeto {

	// ------Atributos------

	private String Nome;
	Simulador simulador = new Simulador();

	// ------Construtor------

	public Projeto(String nome) {
		this.Nome = nome;
	}
	// ------Metodos------

	// Função para salvar os dados no arquivo
	public void SalvarDados(double RigidezSuspensao, double MassaSuspensa, double MassaNaoSuspensa,
			double Amortecimento, double RigidezPneu) {
		try {

			// Nome da pasta
			String diretorio = "saves";
			File pasta = new File(diretorio);

			// Cria a pasta "saves" se não existir
			if (!pasta.exists()) {
				if (pasta.mkdir()) {
					// Se conseguir criar
					System.out.println("Pasta saves criada");
				} else {
					// Se não conseguir criar
					System.out.println("Pasta saves não foi criada");
					return;
				}
			}

			// Caminho do arquivo
			String caminhoArquivo = diretorio + File.separator + Nome + ".txt";
			// Cria o arquivo com o nome desejado
			File arquivo = new File(caminhoArquivo);

			// Verifica se o arquivo já existe
			if (arquivo.exists()) {
				System.out.println("O arquivo já existe");
				return;
			}

			// Cria o arquivo e escreve todas as informações
			if (arquivo.createNewFile()) {
				try (FileWriter escritor = new FileWriter(arquivo)) {
					escritor.write("" + RigidezSuspensao + "\n");
					escritor.write("" + MassaSuspensa + "\n");
					escritor.write("" + MassaNaoSuspensa + "\n");
					escritor.write("" + Amortecimento + "\n");
					escritor.write("" + RigidezPneu + "\n");
				}
			} else {
				// Se não conseguir, escreve para o usuario
				System.out.println("Arquivo não foi criado");
			}
		} catch (IOException e) {
			// Trata os erros de entrada e saída de dados
			System.out.println("Erro na entrada ou saída de arquivos");
		}
	}

	// Função para ler os dados do arquivo
	public double[] LerDados() {

		// Vetor que armazena as informações
		double[] valores = new double[5];

		try {
			// Cria um arquivo com o nome dejesado
			String CaminhoDoArquivo = "saves" + File.separator + Nome + ".txt";
			File arquivo = new File(CaminhoDoArquivo);

			// Vê se o arquivo existe
			if (arquivo.exists()) {
				try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
					// Lê cada um dos 5 parametros
					for (int i = 0; i < 5; i++) {
						// Lê a proxima linha
						String linha = leitor.readLine();
						// Se a linha não for vazia
						if (linha != null) {
							// Converte a linha para double
							valores[i] = Double.parseDouble(linha);
						}
					}
				}
			}
		} catch (IOException e) {
			// IOException e trata os erros de entrada e saída do arquivo
			System.out.println("Erro na entrada ou saída de arquivos");
		} catch (NumberFormatException e) {
			// NumberFormatException e trata os erros de conversão do arquivo
			System.out.println("Erro na conversão de arquivos");
		}
		// Retorna o vetor dos valores
		return valores;
	}
}
