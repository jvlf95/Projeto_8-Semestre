package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.Reserved;
import com.uninove.projeto.projetojoao.repository.ReservedRepository;
import com.uninove.projeto.projetojoao.repository.ResourceRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/reserved")
@CrossOrigin(origins="*")
public class ControllerReserved {

    private final ReservedRepository reservedRepository;

    public ControllerReserved(ReservedRepository reservedRepository) {
        this.reservedRepository = reservedRepository;
    }


    @GetMapping(path="/all")
    public Iterable<Reserved> getAll(){
        return reservedRepository.findAll();
    }
}
