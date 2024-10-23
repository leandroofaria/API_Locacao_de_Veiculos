package com.locacao.controller;

import com.locacao.entity.User;
import com.locacao.repository.UserRepository;
import com.locacao.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema. O nome de usuário deve ser único, e a senha será criptografada antes de ser armazenada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto do usuário contendo 'username' e 'password'. Ambos os campos são obrigatórios.",
            required = true,
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso!");
        return response;
    }

    @Operation(
            summary = "Autenticação de Usuário",
            description = "Autentica um usuário no sistema com base no nome de usuário e senha fornecidos. Se as credenciais forem válidas, um token JWT será gerado e retornado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Nome de usuário ou senha inválidos", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto do usuário contendo 'username' e 'password'. Ambos os campos são obrigatórios.",
            required = true,
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        Map<String, String> response = new HashMap<>();

        if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
        } else {
            response.put("message", "Nome de usuário ou senha inválidos.");
        }

        return response;
    }

    // Atualiza apenas o nome de usuário OU a senha, conforme o campo enviado
    @Operation(summary = "Edita o nome de usuário ou a senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário editado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/edit/{id}")
    public Map<String, String> editUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Optional<User> existingUser = userRepository.findById(id);

        Map<String, String> response = new HashMap<>();

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Verifica se o nome de usuário foi fornecido e atualiza
            if (updates.containsKey("username") && !updates.get("username").isBlank()) {
                user.setUsername(updates.get("username"));
            }

            // Verifica se a senha foi fornecida e atualiza (criptografando)
            if (updates.containsKey("password") && !updates.get("password").isBlank()) {
                user.setPassword(passwordEncoder.encode(updates.get("password")));
            }

            userRepository.save(user);
            response.put("message", "Usuário editado com sucesso!");
        } else {
            response.put("message", "Usuário não encontrado.");
        }

        return response;
    }
}
