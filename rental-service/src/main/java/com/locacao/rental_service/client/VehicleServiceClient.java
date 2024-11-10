package com.locacao.rental_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Configura para acessar o caminho correto no vehicle-service
@FeignClient(name = "vehicle-service")
public interface VehicleServiceClient {
    @GetMapping("/vehicles/{id}") // Mantém o caminho atual, pois está correto
    Object getVehicleById(@PathVariable Long id);
}
