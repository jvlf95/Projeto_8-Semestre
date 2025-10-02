package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.User;
import com.uninove.projeto.projetojoao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(path="/users")
public class ControllerUser {

    private final UserRepository userRepository;

    public ControllerUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/all")
    public Iterable<User> getAll(){
        return userRepository.findAll();
    }
}
