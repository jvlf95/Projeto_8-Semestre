package com.uninove.projeto.projetojoao.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.uninove.projeto.projetojoao.entity.Login;

public interface LoginRepository extends CrudRepository <Login, String>{
    Optional<Login> findByUsernameAndPassword(String usermane, String password);
}
