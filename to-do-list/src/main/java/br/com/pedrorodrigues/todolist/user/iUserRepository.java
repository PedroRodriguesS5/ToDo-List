package br.com.pedrorodrigues.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface iUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
}
