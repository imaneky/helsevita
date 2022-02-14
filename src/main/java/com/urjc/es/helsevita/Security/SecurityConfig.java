package com.urjc.es.helsevita.Security;

import com.urjc.es.helsevita.Enums.EnumRolUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    public RepositoryUserDetailsService userDetailsService;


    public static EnumRolUsers rolito;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests()

                .antMatchers("/index", "/login", "/", "/loginError", "/logout", "/exito", "/exito-contacto",

                        "/contact-us", "/faq", "/myhelsevita", "/search-center", "/work-with-us", "/error", "/insurance", "/politica")   //Aquí se ponen las rutas que se permiten a ese rolito (Anónimo en este caso)

                .permitAll()

                // PRIVATE

                // FOR 3 rolitoES
                .antMatchers("/indexAuth", "/loginExito", "/myProfile").hasAnyRole("ADMIN","HEALTHPERSONNEL", "PATIENT")
                // FOR HEALTH PERSONNEL
                .antMatchers("/areaSanitario").hasAnyRole("HEALTHPERSONNEL")
                // FOR PATIENT
                .antMatchers("/appointmentAlreadyExist/**", "/appointment", "/appointmentNotFound", "/citaAgregada", "/cualDoctor", "/areaPaciente",
                        "/mostrarCitas", "/nuevaCita").hasAnyRole("PATIENT")
                // FOR ADMIN
                .antMatchers(  "/asignarNuevoPaciente", "/asignarNuevoSanitario",
                        "/buscarPaciente", "/buscarSanitario",  "/areaAdmin", "/crearPaciente", "/crearSanitario",  "/user-not-found",
                        "/mostrarSanitario", "/userAlreadyExists", "/admin/**", "/mostrar/**").hasAnyRole("ADMIN")
                // FOR ADMIN AND HEALTH PERSONNEL
                .antMatchers("/mostrarPacientes", "/preguntasSinContestar", "/preguntasSinContestar/**", "/contestarPregunta/**").hasAnyRole("ADMIN", "HEALTHPERSONNEL");





        http
                .formLogin()
                .loginPage("/login") //Ruta login
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/loginExitoso") //Página a la que nos lleva al hacer el login
                .failureUrl("/loginError")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")//Url para deslogearse
                .logoutSuccessUrl("/performLogout") //Url de la zona pública
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .headers().frameOptions().disable();//Para poder acceder a la consola de h2

        http
                .headers()
                .xssProtection();

        http.httpBasic();

    }
}
