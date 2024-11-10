package com.locacao.vehicle_service.controller;

import com.locacao.vehicle_service.entity.Vehicle;
import com.locacao.vehicle_service.repository.VehicleRepository;
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

    @PostMapping("/add")
    public Map<String, String> addVehicle(@RequestBody Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Veículo adicionado com sucesso!");
        return response;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

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
