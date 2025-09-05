package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.Login;
import com.uninove.projeto.projetojoao.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping(path="/login")
@CrossOrigin(origins="http://localhost:8085")
public class ControllerLogin {

    @Autowired
    private LoginRepository loginRepository;

    @PostMapping
    public ResponseEntity<String> fazerlogin(@RequestBody Login login){
        Optional<Login> foundUser = loginRepository.findByUsernameAndPassword(login.getUsername(), login.getPassword());

        if (foundUser.isPresent()){
            return new ResponseEntity<>("Login bem-sucedido!", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Usuário ou senha incorretos!", HttpStatus.UNAUTHORIZED);
        }
    }


}
