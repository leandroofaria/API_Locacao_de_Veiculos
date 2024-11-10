package com.locacao.rental_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Configura para acessar o caminho correto no user-service
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/auth/getUserById/{id}") // Atualiza para o caminho correto
    Object getUserById(@PathVariable Long id);
}
