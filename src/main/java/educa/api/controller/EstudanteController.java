package educa.api.controller;

import educa.api.model.Estudante;
import educa.api.repository.EstudanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios/estudantes")
public class EstudanteController {

    @Autowired
    private EstudanteRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping
    public ResponseEntity<List<Estudante>> mostrarEstudantes() {
        List<Estudante> list = repository.findAll();
        return list.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(list);
    }

    @PostMapping
    public ResponseEntity<Estudante> cadastrarProfessor(@RequestBody Estudante estudante) {
        estudante.setSenha(encoder.encode(estudante.getSenha()));
        return ResponseEntity.status(201).body(repository.save(estudante));
    }

    @PostMapping("/login")
    public ResponseEntity<Estudante> loginEstudante(@RequestBody Estudante validaEstudante) {

        Optional<Estudante> optionalEstudante = repository.findByEmail(validaEstudante.getEmail());
        Estudante estudante  = optionalEstudante.get();

        if (!(optionalEstudante.isEmpty()) && encoder.matches(validaEstudante.getSenha(), estudante.getSenha())) {
            repository.updateAuthenticated(true, estudante.getId());
            return ResponseEntity.status(200).body(estudante);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping("/logoff/{id}")
    public ResponseEntity logoffEstudante(@PathVariable int id) {
        List<Estudante> list = repository.findAll();

        for (Estudante estudanteAtual : list) {
            if (estudanteAtual.getId().equals(id)) {
                if (estudanteAtual.isAutenticado()) {
                    repository.updateAuthenticated(false, id);
                    return ResponseEntity.status(200).body(String.format("Logoff do usuário %s concluído", estudanteAtual.getNome()));
                } else {
                    return ResponseEntity.status(401).body(String.format("Usuário %s NÃO está autenticado", estudanteAtual.getNome()));
                }
            }
        }
        return ResponseEntity.status(403).body(String.format("Usuário do Id %d não encontrado", id));
    }
}