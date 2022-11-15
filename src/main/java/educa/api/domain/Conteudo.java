package educa.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConteudo;
    @Size(min = 4)
    private String titulo;
    @URL
    private String url;
    @Size(min = 4)
    private String artigo;
    @Size(min = 3, max = 5000)
    private String texto;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private int tempoEstimado;
    @ManyToOne
    private Usuario autor;
    @ManyToOne
    private Habilidade habilidade ;
    @OneToMany(mappedBy = "conteudo")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Conteudo(int idConteudo, String titulo, String url, String artigo, String texto,
                    LocalDateTime dataCriacao, int tempoEstimado) {
        this.idConteudo = idConteudo;
        this.titulo = titulo;
        this.url = url;
        this.artigo = artigo;
        this.texto = texto;
        this.dataCriacao = dataCriacao;
        this.tempoEstimado = tempoEstimado;
    }



}