package educa.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


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
    @Size(min = 3)
    private String texto;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @NotNull
    @Min(1)
    private Integer tempoEstimado;
//    @OneToMany
//    private Professor professor;
//    @OneToMany
//    private Estudante estudante;
//    @OneToMany
//    private  Habilidade habilidade;

}