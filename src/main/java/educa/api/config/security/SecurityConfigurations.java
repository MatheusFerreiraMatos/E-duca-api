package educa.api.config.security;

import educa.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Config de autenticações (controle de acesso login)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //config de autorizações (quem pode ter acesso ao que)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/usuarios/estudantes/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/usuarios/estudantes/").permitAll()
                .antMatchers(HttpMethod.POST, "/api/usuarios/professores/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/usuarios/professores/").permitAll()
                .antMatchers(HttpMethod.GET, "/api/conteudos/csv").permitAll()
                .antMatchers(HttpMethod.GET, "/api/conteudos/txt").permitAll()
                .antMatchers(HttpMethod.POST, "/api/conteudos/gravar-text").permitAll()
                .antMatchers(HttpMethod.GET, "/api/topicos/").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.POST, "/api/topicos/").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.PUT, "/api/topicos/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.DELETE, "/api/topicos/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.POST, "/api/topicos/respostas/").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.PUT, "/api/topicos/respostas/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.DELETE, "/api/topicos/respostas/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.POST, "/api/conteudos/avaliacoes").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.PUT, "/api/conteudos/avaliacoes/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.DELETE, "/api/conteudos/avaliacoes/**").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.POST, "/api/conteudos/").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.GET, "/api/conteudos/").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.GET, "/api/conteudos/").hasRole("ESTUDANTE")
                .antMatchers(HttpMethod.PUT, "/api/conteudos/**").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.DELETE, "/api/conteudos/**").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.GET, "/api/habilidades/").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated() // o resto bloqueia
                .and().cors()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, repository), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }
}