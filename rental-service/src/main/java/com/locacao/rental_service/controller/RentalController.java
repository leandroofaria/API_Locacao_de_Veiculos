package com.locacao.rental_service.controller;

import com.locacao.rental_service.entity.Rental;
import com.locacao.rental_service.repository.RentalRepository;
import com.locacao.rental_service.client.UserServiceClient;
import com.locacao.rental_service.client.VehicleServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private VehicleServiceClient vehicleServiceClient;

    @Operation(summary = "Inicia uma nova locação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locação iniciada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startRental(@RequestParam Long userId, @RequestParam Long vehicleId) {
        // Verifique se o usuário e o veículo existem
        userServiceClient.getUserById(userId);
        vehicleServiceClient.getVehicleById(vehicleId);

        // Cria e salva a nova locação
        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setVehicleId(vehicleId);
        rental.setStartDate(LocalDateTime.now());
        rentalRepository.save(rental);

        return ResponseEntity.ok(Map.of("message", "Locação iniciada com sucesso!"));
    }

    @Operation(summary = "Finaliza uma locação em andamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locação finalizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Locação não encontrada")
    })
    @PostMapping("/finish/{id}")
    public ResponseEntity<Map<String, String>> finishRental(@PathVariable Long id) {
        Optional<Rental> rentalOpt = rentalRepository.findById(id);

        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            rental.setEndDate(LocalDateTime.now());
            rentalRepository.save(rental);

            return ResponseEntity.ok(Map.of("message", "Locação finalizada com sucesso!"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Locação não encontrada."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRentalById(@PathVariable Long id) {
        Optional<Rental> rentalOpt = rentalRepository.findById(id);

        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", rental.getId());
            response.put("userId", rental.getUserId());
            response.put("vehicleId", rental.getVehicleId());
            response.put("startDate", rental.getStartDate());
            response.put("endDate", rental.getEndDate());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Locação não encontrada."));
        }
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
