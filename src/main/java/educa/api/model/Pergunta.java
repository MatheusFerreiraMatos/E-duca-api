package educa.api.model;

import javax.validation.constraints.NotBlank;

public class Pergunta {

    @NotBlank
    private String pergunta;

    @NotBlank
    private String opcao1;

    @NotBlank
    private String opcao2;

    @NotBlank
    private String opcao3;

    @NotBlank
    private String opcao4;

    @NotBlank
    private String respostaCorreta;
}
