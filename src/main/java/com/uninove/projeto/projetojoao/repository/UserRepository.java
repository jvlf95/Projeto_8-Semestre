package com.uninove.projeto.projetojoao.repository;

import org.springframework.data.repository.CrudRepository; // Referencia Create, Read, Update e Delete
import com.uninove.projeto.projetojoao.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
}
