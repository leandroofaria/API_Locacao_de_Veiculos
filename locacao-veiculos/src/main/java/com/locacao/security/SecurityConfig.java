package com.locacao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

//Essa classe define como as requisições HTTP são protegidas, quais endpoints são públicos, como as senhas são criptografadas, e define que a aplicação vai funcionar em um modo stateless (sem sessão, usando tokens JWT para autenticação).
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;  // Injeta o filtro JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilitando CSRF (não necessário para APIs REST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/authenticate").permitAll()  // Cadastro e login são publicos
                        .anyRequest().authenticated()  // Todos os outros endpoints precisam de autenticação
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Definir sessão stateless
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Criptografia de senha de usuários
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usando BCrypt para codificar senhas
    }

    // Bean para o AuthenticationManager, necessário para autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}