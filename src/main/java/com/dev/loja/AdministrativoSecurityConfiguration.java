package com.dev.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(2)
public class AdministrativoSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    protected AdministrativoSecurityConfiguration() {
        super();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //usado mais a nivel de testes
//        auth.inMemoryAuthentication().withUser("user")
//                .password(new BCryptPasswordEncoder().encode("123")).authorities("vendedor")
//                .and().withUser("admin")
//                .password(new BCryptPasswordEncoder().encode("admin")).authorities("gerente", "vendedor");

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
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/administrativo/**/cadastrar").hasAnyAuthority("gerente")
                .antMatchers("/administrativo/**").authenticated()
                .and().formLogin().loginPage("/login").failureUrl("/login").loginProcessingUrl("/admin").defaultSuccessUrl("/administrativo")
                .usernameParameter("username").passwordParameter("password")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/administrativo/logout")).logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .and().exceptionHandling().accessDeniedPage("/negadoAdministrativo");
    }
}
