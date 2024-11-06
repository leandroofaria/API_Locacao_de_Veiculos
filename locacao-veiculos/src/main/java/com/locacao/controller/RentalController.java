package com.locacao.controller;

import com.locacao.entity.Rental;
import com.locacao.entity.User;
import com.locacao.entity.Vehicle;
import com.locacao.repository.RentalRepository;
import com.locacao.repository.UserRepository;
import com.locacao.repository.VehicleRepository;
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
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // Endpoint para iniciar uma nova locação
    @Operation(summary = "Inicia uma nova locação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locação iniciada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startRental(@RequestParam Long userId, @RequestParam Long vehicleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        // Verifica se o usuário e o veículo existem e se o veículo está disponível
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuário não encontrado."));
        }
        if (vehicleOpt.isEmpty() || !vehicleOpt.get().isDisponivel()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Veículo não disponível para locação."));
        }

        // Cria e salva a nova locação
        Rental rental = new Rental();
        rental.setUser(userOpt.get());
        rental.setVehicle(vehicleOpt.get());
        rental.setStartDate(LocalDateTime.now());
        rentalRepository.save(rental);

        // Marca o veículo como indisponível
        Vehicle vehicle = vehicleOpt.get();
        vehicle.setDisponivel(false);
        vehicleRepository.save(vehicle);

        return ResponseEntity.ok(Map.of("message", "Locação iniciada com sucesso!"));
    }

    // Endpoint para finalizar uma locação
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

            // Marca o veículo como disponível
            Vehicle vehicle = rental.getVehicle();
            vehicle.setDisponivel(true);
            vehicleRepository.save(vehicle);

            return ResponseEntity.ok(Map.of("message", "Locação finalizada com sucesso!"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Locação não encontrada."));
        }
    }

    // Endpoint para buscar uma locação pelo ID
    @Operation(summary = "Consulta uma locação pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locação encontrada"),
            @ApiResponse(responseCode = "404", description = "Locação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRentalById(@PathVariable Long id) {
        Optional<Rental> rentalOpt = rentalRepository.findById(id);

        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", rental.getId());
            response.put("userId", rental.getUser().getId());
            response.put("vehicleId", rental.getVehicle().getId());
            response.put("startDate", rental.getStartDate());
            response.put("endDate", rental.getEndDate());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Locação não encontrada."));
        }
    }

    // Endpoint para listar todas as locações
    @Operation(summary = "Lista todas as locações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de locações retornada com sucesso")
    })
    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
