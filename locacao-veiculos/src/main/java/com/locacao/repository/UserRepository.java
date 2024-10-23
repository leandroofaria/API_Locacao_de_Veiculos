package com.locacao.repository;

import com.locacao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Esse é um repositório JPA que permite realizar operações de CRUD na entidade User sem escrever SQL. O Spring Data JPA gera essas operações automaticamente. Também incluimos um método personalizado findByUsername, que busca usuários pelo nome de usuário
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
