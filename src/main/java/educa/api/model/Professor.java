package educa.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Professor extends Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nome;

    @Past
    @NotNull
    private LocalDate dtNasc;

    @NotBlank
    private String areaAtuacao;

    @NotNull
    private int tempoCarreira;

    @Email
    private String email;

    @Size(min = 8)
    private String senha;




}
