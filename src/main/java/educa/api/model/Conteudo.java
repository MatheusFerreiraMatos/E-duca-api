package educa.api.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data

public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConteudo;

    @NotBlank
    private String titulo;

    @NotBlank
    private String tipo;

    @NotBlank
    private String url;

    @NotNull
    private int quatCurtidas;


}
