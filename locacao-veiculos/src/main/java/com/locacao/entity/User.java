package com.locacao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

// Essa classe mapeia para a tabela users no banco de dados. Ela contém três campos: id, username, e password
public class User {

    // ID gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O username deve ser único e não pode ser vazio
    @Column(nullable = false, unique = true)
    private String username;

    // A senha é obrigatoria e não pode ser nula
    @Column(nullable = false)
    private String password;
}
