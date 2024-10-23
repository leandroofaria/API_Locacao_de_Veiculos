package com.locacao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
    public class ProtectedController {

        // Esse endpoint é protegido por mecanismos de autenticação do Spring Boot (como JWT). Apenas usuários autenticados podem acessá-lo. Quando um usuário autenticado faz uma requisição, ele recebe a mensagem "Este é um endpoint protegido. Você está autenticado!", confirmando que ele foi validado e autorizado a acessar o recurso.
        @GetMapping("/protected")
        public String protectedEndpoint() {
            return "Este é um endpoint protegido. Você está autenticado!";
        }
    }