package com.uninove.projeto.projetojoao;

import org.springframework.data.repository.CrudRepository; // Referencia Create, Read, Update e Delete
import com.uninove.projeto.projetojoao.User;

public interface UserRepository extends CrudRepository<User, Integer>{
}
