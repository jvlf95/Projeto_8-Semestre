package com.uninove.projeto.projetojoao.repository;

import org.springframework.data.repository.CrudRepository;
import com.uninove.projeto.projetojoao.entity.Reserved;

public interface ReservedRepository extends CrudRepository<Reserved, Integer>{
    //public Iterable<Reserved> existsById(String id);
}
