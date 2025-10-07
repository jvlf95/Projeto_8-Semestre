package com.uninove.projeto.projetojoao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
 // Referencia Create, Read, Update e Delete
import com.uninove.projeto.projetojoao.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
