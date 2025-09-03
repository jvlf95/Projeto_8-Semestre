package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.User;
import com.uninove.projeto.projetojoao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/user") // indica que o começo da URL é /user (depois do caminho da aplicação)
public class ControllerUser {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add") // caminho para adicionar usuários
    @ResponseBody // indica que a String de retorno é a resposta, não referencia o nome de uma tela HTML
    public String addUser(@RequestParam String nome, String condicao, int idade, float peso){
        User n = new User();
        n.setNome(nome);
        n.setCondicao(condicao);
        n.setIdade(idade);
        n.setPeso(peso);
        userRepository.save(n);
        return "Saved";
    }

    @PostMapping(path = "/update")
    //@ResponseBody


    @GetMapping(path = "/all")
    @ResponseBody
    public Iterable<User> getAll(){
        return userRepository.findAll(); // retorna um JSON ou XML
    }

}
