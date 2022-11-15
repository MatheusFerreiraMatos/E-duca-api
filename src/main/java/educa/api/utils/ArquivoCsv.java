package educa.api.utils;

import educa.api.domain.Conteudo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Scanner;



    public class ArquivoCsv {

        Conteudo conteudo = new Conteudo();
        public static void gravaArquivoCsv(ListObj<Conteudo> lista,
                                           String nomeArq) {


            FileWriter arq = null;  // objeto que representa o arquivo de escrita
            Formatter saida = null; // objeto usado para escrever no arquivo
            Boolean deuRuim = false;
            nomeArq += ".csv";      // acrescenta a extensão csv ao nome do arquivo

            // Bloco try-catch para abrir o arquivo
            try {
                arq = new FileWriter(nomeArq);
                saida = new Formatter(arq);
            }
            catch (IOException erro) {
                System.out.println("Erro ao abrir o arquivo");
                System.exit(1);
            }

            // Bloco try-catch para gravar o arquivo
            try {
                for (int i = 0; i < lista.getTamanho(); i++) {
                    Conteudo c = lista.getElemento(i);
                    saida.format("%s;%s;%s;%s;%s;%d\n",c.getTitulo(),c.getUrl(),
                            c.getArtigo(),c.getTexto(), c.getDataCriacao(), c.getTempoEstimado());
                }
            }
            catch (FormatterClosedException erro) {
                System.out.println("Erro ao gravar o arquivo");
                erro.printStackTrace();
                deuRuim = true;
            }
            finally {
                saida.close();
                try {
                    arq.close();
                }
                catch (IOException erro) {
                    System.out.println("Erro ao fechar o arquivo");
                    deuRuim = true;
                }
                if (deuRuim) {
                    System.exit(1);
                }
            }
        }

        public static void leExibeArquivoCsv(String nomeArq) {
            FileReader arq = null;  // objeto que representa o arquivo para leitura
            Scanner entrada = null; // objeto usado para ler do arquivo
            Boolean deuRuim = false;
            nomeArq += ".csv";

            // Bloco try-catch para abrir o arquivo
            try {
                arq = new FileReader(nomeArq);
                entrada = new Scanner(arq).useDelimiter(";|\\n");
            }
            catch (FileNotFoundException erro) {
                System.out.println("Arquivo não encontrado");
                System.exit(1);
            }

            // Bloco para ler o arquivo
            try {
                System.out.printf("%-6S %-3S %6S %5S %11s %13s\n", "Título", "Url",
                        "Artigo", "Texto", "DataCriação", "TempoEstimado");
                while (entrada.hasNext()) {
                    String titulo = entrada.next();
                    String url = entrada.next();
                    String artigo = entrada.next();
                    String texto = entrada.next();
                    String dataCriacao = entrada.next();
                    int tempoEstimado = entrada.nextInt();
                    System.out.printf("%-6S %-3S %6S %5S %11s %13s\n", titulo, url,
                            artigo, texto, dataCriacao, tempoEstimado);
                }
            }
            catch (NoSuchElementException erro) {
                System.out.println("Arquivo com problemas");
                System.out.println(erro);
                erro.printStackTrace();
                deuRuim = true;
            }
            catch (IllegalStateException erro) {
                System.out.println("Erro na leitura do arquivo");
                System.out.println(erro);
                erro.printStackTrace();
                deuRuim = true;
            }
            finally {
                entrada.close();
                try {
                    arq.close();
                }
                catch (IOException erro) {
                    System.out.println("Erro ao fechar o arquivo");
                    deuRuim = true;
                }
                if (deuRuim) {
                    System.exit(1);
                }
            }
        }




        public static void main(String[] args) {
            ListObj<Conteudo> lista = new ListObj(10);

            lista.adiciona(new Conteudo(1, "Matemática", "aulasdematematica.com/aulas/conteudo", "Material de apoio",
                    "1. Aulas de matemática aplicada", LocalDateTime.now() , 10));
            lista.adiciona(new Conteudo(2, "Matemática", "aulasdematematica.com/aulas/conteudo", "Material de apoio",
                    "2. Aulas de matemática aplicada", LocalDateTime.now() , 10));


            lista.exibe();
            gravaArquivoCsv(lista, "produto");
            leExibeArquivoCsv("produto");
        }
    }


