package com.uninove.projeto.projetojoao.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uninove.projeto.projetojoao.entity.Login;

public interface LoginRepository extends JpaRepository <Login, String>{
    public Login findByUsernameAndPassword(String username, String password);
}
