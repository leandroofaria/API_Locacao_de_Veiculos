package com.locacao.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // Injeta a classe JwtUtil que será usada para manipular tokens JWT (gerar, extrair e validar)

    @Autowired
    private UserDetailsService userDetailsService;  // Injeta o UserDetailsService para carregar os detalhes do usuário a partir do banco de dados

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o cabeçalho Authorization da requisição
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Verifica se o cabeçalho Authorization contém o token e se ele começa com 'Bearer '
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Remove o prefixo 'Bearer ' do token
            try {
                // Extrai o nome de usuário do token
                username = jwtUtil.extractUsername(jwt);
                System.out.println("[INFO] Token recebido: " + jwt);
                System.out.println("[INFO] Nome de usuário extraído do token: " + username);
            } catch (ExpiredJwtException e) {
                System.out.println("[ERROR] Token expirado: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[ERROR] Erro ao processar o token JWT: " + e.getMessage());
            }
        } else {
            System.out.println("[WARNING] Nenhum token JWT encontrado no cabeçalho Authorization.");
        }

        // Verifica se o nome de usuário foi extraído do token e se o usuário ainda não está autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("[INFO] Usuário extraído do token: " + username);
            try {
                // Carrega os detalhes do usuário do banco de dados com base no nome de usuário extraído do token
                var userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("[INFO] Detalhes do usuário carregados com sucesso.");

                // Se o token for válido (ou seja, não expirado e emitido para o usuário correto)
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    // Cria um objeto UsernamePasswordAuthenticationToken, indicando que o usuário está autenticado
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Adiciona detalhes adicionais de autenticação à requisição
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Define o usuário autenticado no contexto de segurança do Spring
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("[INFO] Autenticação realizada com sucesso para o usuário: " + username);
                } else {
                    System.out.println("[ERROR] Token inválido para o usuário: " + username);
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Erro ao carregar detalhes do usuário ou ao validar token: " + e.getMessage());
            }
        } else if (username == null) {
            System.out.println("[WARNING] O nome de usuário extraído do token JWT é nulo.");
        } else {
            System.out.println("[INFO] Usuário já está autenticado: " + username);
        }

        // Continua o processamento da requisição após o filtro JWT
        chain.doFilter(request, response);
    }
}
