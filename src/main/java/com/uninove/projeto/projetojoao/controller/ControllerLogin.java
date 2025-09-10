package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.LoginDto;
import com.uninove.projeto.projetojoao.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController // informa que é uma REST API
@RequestMapping(path="/inicio") // informa o endereço
@CrossOrigin(origins="http://localhost:8081")
public class ControllerLogin {

    @Autowired
    private final LoginService loginService; // acessar a base de dados

    public ControllerLogin(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        boolean isValid = loginService.validateLogin(loginDto.getUsername(), loginDto.getPassword());

        if(isValid){
            return ResponseEntity.ok("Login Realizado com sucesso!");
        }else{
            return ResponseEntity.status(401).body("Usuário e/ou senha inválidos!");
        }


    }

}
