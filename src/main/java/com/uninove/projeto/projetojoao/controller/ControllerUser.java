package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.User;
import com.uninove.projeto.projetojoao.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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

    @GetMapping(path="/all/{id}")
    public Optional<User> getUser(@PathVariable int id){
        return userRepository.findById(id);
    }

    @PatchMapping(path="/patch/{id}")
    public User updateUser(@PathVariable int id,@RequestBody User requestUser){
        return userRepository.save(requestUser);
    }

    @DeleteMapping(path="/delete/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping(path="/create")
    public User setUser(@RequestBody User requestUser){
        return userRepository.save(requestUser);
    }
}
