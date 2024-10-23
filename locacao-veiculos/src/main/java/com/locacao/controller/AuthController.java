package com.locacao.controller;

import com.locacao.entity.User;
import com.locacao.repository.UserRepository;
import com.locacao.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //Acessa repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    //Acessa o encoder da senha
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Responsavel por gerar os tokens
    @Autowired
    private JwtUtil jwtUtil;

    // Esse endpoint permite que novos usuários sejam registrados no sistema. Ele recebe os dados do usuário no formato JSON, criptografa a senha e salva o usuário no banco de dados. A resposta enviada ao cliente é uma mensagem de sucesso.
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso!");
        return response;
    }

    // Esse endpoint permite que um usuário faça login. Ele recebe um nome de usuário e uma senha no formato JSON, valida essas credenciais com os dados armazenados no banco de dados e, se a autenticação for bem-sucedida, gera e retorna um token JWT.
    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        Map<String, String> response = new HashMap<>();

        if (existingUser.isPresent()) {
            System.out.println("Usuário encontrado: " + existingUser.get().getUsername());
        } else {
            System.out.println("Usuário não encontrado");
        }

        // Um token JWT é gerado após a autenticação bem-sucedida. Esse token será usado em requisições subsequentes para acessar endpoints protegidos.
        if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
        } else {
            response.put("message", "Nome de usuário ou senha inválidos.");
        }

        return response;
    }

}
