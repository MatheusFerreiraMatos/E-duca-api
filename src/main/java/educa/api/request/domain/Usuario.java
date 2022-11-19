package educa.api.request.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tb_usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;
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
    @JsonIgnore
    private String senha;
    private String areaAtuacao;
    private LocalDate inicioAtuacao;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
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

    public Usuario(Integer idUsuario, String nome, String sobrenome, LocalDate dataNasc, String email, String areaAtuacao, LocalDate inicioAtuacao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.areaAtuacao = areaAtuacao;
        this.inicioAtuacao = inicioAtuacao;
    }

    public void adicionarPerfil(Perfil novo) {
        this.perfis.add(novo);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.senha;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
