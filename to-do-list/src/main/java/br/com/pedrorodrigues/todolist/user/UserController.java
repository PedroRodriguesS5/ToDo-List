package br.com.pedrorodrigues.todolist.user;

import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// modificadores
// public
// private
// protected

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private iUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            return ResponseEntity.status(400).body("Usuário já existe");
        }
        var paswordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(paswordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(201).body(userCreated);
    }
}
