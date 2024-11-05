package com.locacao.controller;

import com.locacao.entity.Vehicle;
import com.locacao.repository.VehicleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Lista todos os veículos disponíveis
    @Operation(summary = "Lista todos os veículos", description = "Retorna uma lista de todos os veículos disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    })
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();  // Retorna todos os veículos do banco de dados
    }

    // Obtém os detalhes de um veículo específico pelo ID
    @Operation(summary = "Obtém os detalhes de um veículo específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);

        Map<String, Object> response = new HashMap<>();
        if (vehicle.isPresent()) {
            response.put("id", vehicle.get().getId());
            response.put("modelo", vehicle.get().getModelo());
            response.put("marca", vehicle.get().getMarca());
            response.put("ano", vehicle.get().getAno());
            response.put("disponivel", vehicle.get().isDisponivel());

            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Veículo não encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Método para deletar um veículo pelo ID
    @Operation(summary = "Deleta um veículo específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteVehicle(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            response.put("message", "Veículo deletado com sucesso!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Veículo não encontrado.");
            return ResponseEntity.status(404).body(response);
        }
    }
}
