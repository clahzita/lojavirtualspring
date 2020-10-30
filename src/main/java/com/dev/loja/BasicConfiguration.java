package com.dev.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    protected BasicConfiguration() {
        super();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //usado mais a nivel de testes
        auth.inMemoryAuthentication().withUser("user")
                .password(new BCryptPasswordEncoder().encode("123")).authorities("vendedor")
                .and().withUser("admin")
                .password(new BCryptPasswordEncoder().encode("admin")).authorities("gerente", "vendedor");

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email as username, senha as password, 1 as enable from funcionario where email=?")
                .authoritiesByUsernameQuery(
                        "select funcionario.email as username, papel.nome as authorithy from permissao " +
                                "inner join funcionario on funcionario.id=permissao.funcionario_id " +
                                "inner join papel on permissao.papel_id=papel.id " +
                                "where funcionario.email=?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
//                .antMatchers("/administrativo/entrada/cadastro").permitAll()
                .antMatchers("/administrativo/entrada/**").hasAuthority("gerente")
                .antMatchers("/administrativo/**").hasAnyAuthority("gerente", "vendedor")
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/administrativo")
                .and().exceptionHandling().accessDeniedPage("/negado");
    }
}
