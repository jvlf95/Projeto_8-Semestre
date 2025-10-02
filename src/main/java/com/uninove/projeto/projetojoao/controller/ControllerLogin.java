package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.Login;
import com.uninove.projeto.projetojoao.repository.LoginRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController // informa que é uma REST API
@RequestMapping(path="/login") // informa o endereço
@CrossOrigin(origins="*")
public class ControllerLogin {

    private final LoginRepository loginRepository; // acessar a base de dados

    public ControllerLogin(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }

    @PostMapping(path="/home")
    public ResponseEntity<Login> testLogin(@RequestBody Login loginRequest){
        var login = loginRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if(login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(login);

    }


    // testar endpoint
    /*@GetMapping(path="/all")
    public Iterable<Login> getAll(){
        return loginRepository.findAll();
    }*/

}
