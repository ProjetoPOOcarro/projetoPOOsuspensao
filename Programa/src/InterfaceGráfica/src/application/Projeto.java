package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Projeto {

    private String Nome;

    public Projeto(String nome) {
        this.Nome = nome;
    }

    // Função para salvar os dados no arquivo
    public void SalvarDados(double RigidezSuspensao, double MassaSuspensa, double MassaNaoSuspensa, double Amortecimento, double RigidezPneu) {
        try {
            String diretorio = "saves";
            File pasta = new File(diretorio);

            // Cria a pasta "saves" se não existir
            if (!pasta.exists()) {
                if (pasta.mkdir()) {
                    System.out.println("Pasta 'saves' criada com sucesso.");
                } else {
                    System.out.println("Não foi possível criar a pasta 'saves'.");
                    return;
                }
            }

            // Caminho do arquivo
            String caminhoArquivo = diretorio + File.separator + Nome + ".txt";
            File arquivo = new File(caminhoArquivo);

            // Verifica se o arquivo já existe
            if (arquivo.exists()) {
                System.out.println("O arquivo já existe: " + caminhoArquivo);
                return; // Interrompe a execução para evitar sobrescrever o arquivo
            }

            // Cria o arquivo e escreve os dados
            if (arquivo.createNewFile()) {
                try (FileWriter escritor = new FileWriter(arquivo)) {
                    escritor.write("" + RigidezSuspensao + "\n");
                    escritor.write("" + MassaSuspensa + "\n");
                    escritor.write("" + MassaNaoSuspensa + "\n");
                    escritor.write("" + Amortecimento + "\n");
                    escritor.write("" + RigidezPneu + "\n");
                    System.out.println("Dados iniciais salvos no arquivo.");
                }
            } else {
                System.out.println("Não foi possível criar o arquivo.");
            }
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao manipular o arquivo: " + e.getMessage());
        }
    }

    // Função para ler os dados do arquivo
    public double[] LerDados() {
        double[] valores = new double[5]; // Array para armazenar os 5 números
        try {
            String caminhoArquivo = "saves" + File.separator + Nome + ".txt";
            File arquivo = new File(caminhoArquivo);

            // Verifica se o arquivo existe antes de tentar ler
            if (arquivo.exists()) {
                try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
                    for (int i = 0; i < 5; i++) {
                        String linha = leitor.readLine();
                        if (linha != null) {
                            valores[i] = Double.parseDouble(linha); // Converte a linha para double
                        }
                    }
                    System.out.println("Dados lidos com sucesso do arquivo.");
                }
            } else {
                System.out.println("O arquivo não existe: " + caminhoArquivo);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter dados do arquivo: " + e.getMessage());
        }
        return valores;
    }

}

