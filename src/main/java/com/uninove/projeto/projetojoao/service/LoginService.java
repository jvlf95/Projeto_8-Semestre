package com.uninove.projeto.projetojoao.service;

import com.uninove.projeto.projetojoao.entity.Login;
import com.uninove.projeto.projetojoao.repository.LoginRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service // classe responsável pelas regras de negócios
public class LoginService {

    @Autowired // fala pro springboot tomar conta da instância da classe
    private LoginRepository loginRepository; // usa a classe LoginRepository para acessar as informações do banco de dados da classe Entity
    private BCryptPasswordEncoder passwordEncoder;

    public LoginService(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // novo encoder
    }


    public boolean validateLogin(String username, String password){
        Optional<Login> loginOpt = loginRepository.findById(username);
        // usando o Jpapository o Spring cria automaticamente os métodos necessários

        if (loginOpt.isPresent()){
            Login login = loginOpt.get();
            return passwordEncoder.matches(password, login.getPassword());
        }
        return false;


    }


}
