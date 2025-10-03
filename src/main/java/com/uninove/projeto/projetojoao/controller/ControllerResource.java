package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.Resource;
import com.uninove.projeto.projetojoao.repository.ResourceRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/resource")
@CrossOrigin(origins="*")
public class ControllerResource {

    private final ResourceRepository resourceRepository;

    public ControllerResource(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping(path="/all")
    public Iterable<Resource> getAll(){
        return resourceRepository.findAll();
    }
}
