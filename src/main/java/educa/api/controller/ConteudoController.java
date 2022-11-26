package educa.api.controller;

import educa.api.request.ConteudoRequest;
import educa.api.request.domain.Conteudo;
import educa.api.request.domain.Habilidade;
import educa.api.request.domain.Usuario;
import educa.api.repository.ConteudoRepository;
import educa.api.repository.HabilidadeRepository;
import educa.api.utils.ArquivoCsv;
import educa.api.utils.ArquivoTxt;
import educa.api.utils.ListObj;
import educa.api.utils.PilhaObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoController {

    @Autowired
    private ConteudoRepository repository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    private PilhaObj<Conteudo> pilhaRefazer = new PilhaObj<>(100);

    @PostMapping
    public ResponseEntity<Conteudo> create(
            @RequestBody @Valid Conteudo postagem,
            @AuthenticationPrincipal Usuario usuario) {
        Optional<Habilidade> habilidade = habilidadeRepository.findByCodigo(postagem.getHabilidade().getCodigo());

        if (habilidade.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Habilidade não existe"
            );
        }

        postagem.setUsuario(usuario);
        postagem.setHabilidade(habilidade.get());
        return ResponseEntity.status(201).body(repository.save(postagem));

    }

    @GetMapping
    public ResponseEntity<Page<Conteudo>> read(
            @RequestParam(required = false) String titulo,
            @PageableDefault(sort = "idConteudo", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao
    ) {
        if (titulo == null) {
            Page<Conteudo> conteudos = repository.findAll(paginacao);
            return conteudos.isEmpty()
                    ? ResponseEntity.status(204).build()
                    : ResponseEntity.status(200).body(conteudos);
        } else {
            Page<Conteudo> conteudos = repository.findByTitulo(titulo, paginacao);
            return conteudos.isEmpty()
                    ? ResponseEntity.status(204).build()
                    : ResponseEntity.status(200).body(conteudos);
        }
    }

    @GetMapping("/usuario-secao")
    public  ResponseEntity<Page<Conteudo>> readByConteudoAutor(
            @RequestParam(required = false) String titulo,
            @PageableDefault(sort = "idConteudo", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao,
            @AuthenticationPrincipal Usuario usuario) {

        if (titulo == null) {
            Page<Conteudo> conteudos = repository.findByUsuarioIdUsuario(usuario.getIdUsuario(), paginacao);
            return conteudos.isEmpty()
                    ? ResponseEntity.status(204).build()
                    : ResponseEntity.status(200).body(conteudos);
        } else {
            Page<Conteudo> conteudos = repository.findByTituloAndUsuarioIdUsuario(titulo, usuario.getIdUsuario(), paginacao);
            return conteudos.isEmpty()
                    ? ResponseEntity.status(204).build()
                    : ResponseEntity.status(200).body(conteudos);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Conteudo> update(
            @PathVariable int id,
            @RequestBody @Valid ConteudoRequest postagem,
            @AuthenticationPrincipal Usuario usuario) {
        if (repository.existsById(id)) {
            Optional<Habilidade> habilidade = habilidadeRepository.findByCodigo(postagem.getHabilidade().getCodigo());
            Optional<Conteudo> validaAutor = repository.findById(id);

            if (validaAutor.get().getUsuario().getEmail().equals(usuario.getEmail())) {
                Optional<Conteudo> conteudo = repository.findById(id);
                conteudo.get().setTitulo(postagem.getTitulo());
                conteudo.get().setTexto(postagem.getTexto());
                conteudo.get().setUrlVideo(postagem.getUrlVideo());
                conteudo.get().setUsuario(usuario);
                conteudo.get().setHabilidade(habilidade.get());
                repository.save(conteudo.get());
                return ResponseEntity.status(200).body(conteudo.get());
            }
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário não autorizado para atualização, identificador não compatível"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Identificador não encontrado"
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id, @AuthenticationPrincipal Usuario usuario) {
        Optional<Conteudo> validaAutor = repository.findById(id);
        if (repository.existsById(id)) {
            if (validaAutor.get().getUsuario().getEmail().equals(usuario.getEmail())) {
                pilhaRefazer.push(validaAutor.get());
                repository.deleteById(id);
                return ResponseEntity.status(200).build();
            }
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário não autorizado para atualização, identificador não compatível"
            );
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Identificador não encontrado"
        );
    }

    @GetMapping("/refazer")
    public void refazer(){
        if (pilhaRefazer.isEmpty()) {
            throw new IllegalStateException("Pilha vázia!");
        }
        for (int i = 0; i < pilhaRefazer.getTopo(); i++) {
            Conteudo conteudo = pilhaRefazer.pop();
            repository.save(conteudo);
        }
    }

    @GetMapping("/order")
    public ResponseEntity<List<Conteudo>> getOrderConteudos() {
        List<Conteudo> list = repository.findAll();
        ListObj<Conteudo> listObj = new ListObj<>(list.size());

        for (Conteudo conteudo : list) {
            listObj.add(conteudo);
        }

        for (int i = 0; i < listObj.getTamanho(); i++) {
            for (int j = i+1; j < listObj.getTamanho(); j++) {
                if (listObj.getElemento(j).getTempoEstimado() < listObj.getElemento(i).getTempoEstimado()) {
                    Conteudo aux = listObj.getElemento(i);
                    listObj.adicionaIndice(i, listObj.getElemento(j));
                    listObj.adicionaIndice(j, aux);
                }
            }
        }

        return listObj.getTamanho() == 0
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(listObj.all());
    }

    @GetMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<byte[]> gerarCsv() throws IOException {
        List<Conteudo> conteudos = repository.findAll();
        ListObj<Conteudo> listObj = new ListObj<>(100);

        for (Conteudo conteudo : conteudos) {
            listObj.add(conteudo);
        }

        ArquivoCsv arquivoCsv = new ArquivoCsv();

        arquivoCsv.gravaArquivoCsv(listObj, "conteudos");

        File file = new File("conteudos.csv");

        byte[] bytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.status(200).header("content-disposition", "attachment; filename=\"conteudos.csv\"").body(bytes);
    }

    @GetMapping(value = "/txt", produces = "text/txt")
    public ResponseEntity<byte[]> gerarTxt() throws IOException {
        List<Conteudo> conteudos = repository.findAll();

        List<Usuario> autorTxt = new ArrayList<>();
        List<Conteudo> conteudosTxt = new ArrayList<>();

        for (Conteudo conteudo : conteudos) {
            autorTxt.add(conteudo.getUsuario());
            conteudosTxt.add(conteudo);
        }

        ArquivoTxt arquivoTxt = new ArquivoTxt();

        arquivoTxt.gravaArquivoTxt(autorTxt, conteudosTxt, "conteudos.txt");

        File file = new File("conteudos.txt");

        byte[] bytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.status(200).header("content-disposition", "attachment; filename=\"conteudos.txt\"").body(bytes);
    }

    @PostMapping(value = "/gravar-text", consumes = "text/plain")
    public ResponseEntity<Void> gravarTxt(@RequestBody byte[] novoArquivo) {
        String arquivoConverte = new String(novoArquivo);

        ArquivoTxt arquivoTxt = new ArquivoTxt();

        arquivoTxt.leArquivoTxt(arquivoConverte);

        return ResponseEntity.status(200).build();
    }

}
