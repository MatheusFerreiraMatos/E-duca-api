package educa.api.request.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Table(name = "tb_conteudo")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConteudo;
    @Size(min = 4)
    @NotBlank
    private String titulo;
    @Size(min = 3, max = 5000)
    private String texto;
    @URL
    private String urlVideo;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @NotNull
    private int tempoEstimado;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    @NotNull
    private Habilidade habilidade;
    @OneToMany(mappedBy = "conteudo")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Conteudo(int idConteudo, String titulo, String urlVideo, String texto, LocalDateTime dataCriacao, int tempoEstimado) {
        this.idConteudo = idConteudo;
        this.titulo = titulo;
        this.texto = texto;
        this.urlVideo = urlVideo;
        this.dataCriacao = dataCriacao;
        this.tempoEstimado = tempoEstimado;
    }

}