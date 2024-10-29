package com.locacao.controller;

import com.locacao.entity.Vehicle;
import com.locacao.repository.VehicleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    // Endpoint para adicionar um novo veículo
    @Operation(summary = "Adiciona um novo veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados do veículo inválidos")
    })
    @PostMapping("/add")
    public Map<String, String> addVehicle(@RequestBody Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Veículo adicionado com sucesso!");
        return response;
    }

    @Operation(summary = "Lista todos os veículos", description = "Retorna uma lista de todos os veículos disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    })
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();  // Retorna todos os veículos do banco de dados
    }
}
