package educa.api.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
public class Estudante extends Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nome;


    @Past
    @NotNull
    private LocalDate dtNasc;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String senha;




}
