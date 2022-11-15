package educa.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 3)
    @NotBlank
    private String nome;
    @Size(min = 3)
    @NotBlank
    private String sobrenome;
    @Past
    @NotNull
    private LocalDate dataNasc;
    @Column(unique = true)
    @Email
    @NotBlank
    private String email;
    @Size(min = 8)
    @NotBlank
    private String senha;
    private String areaAtuacao;
    private LocalDate inicioAtuacao;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario(String nome, String sobrenome, LocalDate dataNasc, String email, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nome, String sobrenome, LocalDate dataNasc, String email, String senha, String areaAtuacao, LocalDate inicioAtuacao) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
        this.areaAtuacao = areaAtuacao;
        this.inicioAtuacao = inicioAtuacao;
    }

    public Usuario(Integer id, String nome, String sobrenome, LocalDate dataNasc, String email, String areaAtuacao,
                   LocalDate inicioAtuacao) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.areaAtuacao = areaAtuacao;
        this.inicioAtuacao = inicioAtuacao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
