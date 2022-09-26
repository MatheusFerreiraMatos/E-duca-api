package educa.api.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Resposta {

    @NotBlank
    private String resposta;

}
