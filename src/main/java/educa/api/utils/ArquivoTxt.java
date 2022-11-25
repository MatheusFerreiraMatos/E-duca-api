package educa.api.utils;

import educa.api.request.domain.Conteudo;
import educa.api.request.domain.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArquivoTxt {

    Conteudo conteudo =new Conteudo();

    Usuario autor = new Usuario();

    public static void gravaRegistro(String registro, String nomeArq) {
        BufferedWriter saida = null;


        // try-catch para abrir o arquivo
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq,true));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
        }

        // try-catch para gravar e fechar o arquivo
        try {
            saida.append(registro + "\n");
            saida.close();
        }
        catch (IOException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
        }
    }

    public static void gravaArquivoTxt(List<Usuario> listaAutor, List<Conteudo> listaCont,  String nomeArq) {
        int contaRegDados = 0;

        // Monta o registro de header
        String header = "CONTEUDO";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "00";

        // Grava o registro de header
        gravaRegistro(header, nomeArq);

        // Monta e grava os registros de corpo (ou de dados)
        String corpo;
        for (Usuario autor : listaAutor ) {
            corpo = "03";
            corpo += String.format("%-4.5s", autor.getIdUsuario());
            corpo += String.format("%-20.8s", autor.getNome());
            corpo += String.format("%-50.50s", autor.getSobrenome());
            corpo += String.format("%-40.40s", autor.getEmail());
            corpo += String.format("%-25.25s", autor.getDataNasc());
            corpo += String.format("%-25.25s", autor.getAreaAtuacao());
            corpo += String.format("%-25.25s", autor.getInicioAtuacao());

            gravaRegistro(corpo, nomeArq);
            contaRegDados++;
        }


        for (Conteudo conteudo1 : listaCont) {
            corpo = "02";
            corpo += String.format("%-4.5s", conteudo1.getIdConteudo());
            corpo += String.format("%-20.12s", conteudo1.getTitulo());
            corpo += String.format("%-50.50s", conteudo1.getUrlVideo());
            corpo += String.format("%-25.50s", conteudo1.getTexto());
            corpo += String.format("%-25.25s", conteudo1.getDataCriacao());
            corpo += String.format("%03d", conteudo1.getTempoEstimado());
            gravaRegistro(corpo, nomeArq);
            contaRegDados++;
        }

        // Monta e grava o registro de trailer
        String trailer = "01";
        trailer += String.format("%010d", contaRegDados);

        gravaRegistro(trailer, nomeArq);
    }

    public static void leArquivoTxt(String nomeArq) {
        BufferedReader entrada = null;
        String registro, tipoRegistro;
        String titulo, url, texto;
        int idUsuario;
        LocalDate dataCriacao;
        String nome, sobreNome, email, areaAtuacao;

        int tempoEstimado, id;
        int contaRegDadoLido = 0;
        int qtdRegDadoGravado;

        List<Conteudo> listaLida = new ArrayList();
        List<Usuario> listaLidaUser = new ArrayList();

        // try-catch para abrir o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
        }

        // try-catch para ler e fechar o arquivo
        try {
            // Leitura o 1o registro do arquivo
            registro = entrada.readLine();

            while (registro != null) {

                tipoRegistro = registro.substring(0, 2);
                if (tipoRegistro.equals("00")) {
                    System.out.println("Registro de header");
                    System.out.println("Tipo de arquivo: " + registro.substring(3, 10));
                    System.out.println("Data e hora da gravação: " + registro.substring(10, 29));
                    System.out.println("Versão do layout: " + registro.substring(29, 31));
                }
                else if (tipoRegistro.equals("01")) {
                    System.out.println("Registro de trailer");
                    qtdRegDadoGravado = Integer.parseInt(registro.substring(2, 12));
                    System.out.println("Quantidade de reg de dados lidos: " + contaRegDadoLido);
                    System.out.println("Quantidade de reg de dados gravados: " + qtdRegDadoGravado);
                    if (contaRegDadoLido == qtdRegDadoGravado) {
                        System.out.println("Quantidade de registros de dados lidos compatível com "
                                + "quantidade de registros de dados gravados");
                    }
                    else {
                        System.out.println("Quantidade de registros de dados lidos incompatível com "
                                + "quantidade de registros de dados gravados");
                    }
                }
                else if (tipoRegistro.equals("02")) {
                    System.out.println("Registro de corpo");
                    id = Integer.parseInt(registro.substring(2, 4).trim());
                    titulo = registro.substring(4, 52).trim();
                    url = registro.substring(52,152).trim();
                    texto = registro.substring(152, 5152).trim();
                    dataCriacao = LocalDate.ofEpochDay(Integer.parseInt(registro.substring(5152, 5172).trim()));
                    tempoEstimado = Integer.parseInt(registro.substring(5172, 5177).trim());
                    contaRegDadoLido++;

                  // Instancia um objeto Aluno com as informações lidas
                    Conteudo c = new Conteudo(id,titulo, url, texto, dataCriacao.atStartOfDay(), tempoEstimado);

                    // No Projeto de PI, pode fazer
                    // repository.save(a)

                    // No nosso caso, como não estamos conectados ao banco
                    // vamos adicionar o objeto a na listaLida
                    listaLida.add(c);

                }
                else if (tipoRegistro.equals("03")) {
                    System.out.println("Registro de corpo");
                    idUsuario = Integer.parseInt(registro.substring(2, 5).trim());
                    nome = registro.substring(5, 55).trim();
                    sobreNome = registro.substring(55,105).trim();
                    email = registro.substring(105, 150).trim();
                    areaAtuacao = registro.substring(150, 250).trim();
                    contaRegDadoLido++;

                    Usuario usuario = new Usuario(idUsuario, nome, sobreNome, LocalDate.now(),  email, areaAtuacao, LocalDate.now());

                    listaLidaUser.add(usuario);

                    // Instancia um objeto Aluno com as informações lidas


                    // No Projeto de PI, pode fazer
                    // repository.save(a)

                    // No nosso caso, como não estamos conectados ao banco
                    // vamos adicionar o objeto a na listaLida


                }
                else {
                    System.out.println("Tipo de registro inválido!");
                }

                // Lê o próximo registro
                registro = entrada.readLine();
            }
            // Fecha o arquivo
            entrada.close();
        }
        catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        }

        // Exibe a lista lida
        System.out.println("\nConteúdo da lista lida do arquivo:");
        for (Conteudo c : listaLida) {
            System.out.println(c);
        }

        System.out.println("\nConteúdo da lista lida do arquivo:");
        for (Usuario usuario : listaLidaUser) {
            System.out.println(usuario);
        }

    }



    public static void main(String[] args) {
        List<Conteudo> lista = new ArrayList();
        List<Usuario> autor = new ArrayList<>();

        autor.add(new Usuario(1, "Lucas", "Pietro", LocalDate.now(),"lucas@gmail.com.br",
                "Professor", LocalDate.now()));

        lista.add(new Conteudo(1, "Matemática", "aulasdematematica.com/aulas/conteudo",
                "1. Aulas de matemática aplicada", LocalDateTime.now() , 10));
        lista.add(new Conteudo(2, "Matematica", "aulao.com/aulas/execicio",
                "2. resolva os exercícios", LocalDateTime.now() , 20));
        lista.add(new Conteudo(3, "Matemática", "aulao.com/aulas/execicio",
                "3. Aulas de matemática aplicada", LocalDateTime.now() , 20));




        System.out.println("Lista de conteúdos:");
        for (Conteudo c : lista) {
            System.out.println(c);
        }
        System.out.println();
        System.out.println("======================================================================================================================================");
        System.out.println();

        System.out.println("Lista de usuários:");
        for (Usuario u : autor ){
            System.out.println(u);
        }

        gravaArquivoTxt(autor, lista , "conteudo.txt");
//        leArquivoTxt("conteudo.txt");
    }
}