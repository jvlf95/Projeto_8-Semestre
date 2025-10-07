package com.uninove.projeto.projetojoao.controller;

import com.uninove.projeto.projetojoao.entity.Resource;
import com.uninove.projeto.projetojoao.repository.ResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


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

    @GetMapping(path="/all/{id}")
    public Optional<Resource> getResource(@PathVariable int id){
        return resourceRepository.findById(id);
    }

    @PatchMapping(path="/patch/{id}")
    public Resource updateResource(@PathVariable int id, @RequestBody Resource requestResource){
        return resourceRepository.save(requestResource);
    }

    @DeleteMapping(path="/delete/{id}")
    public void deleteResource(@PathVariable int id){
        resourceRepository.deleteById(id);
    }

    @PostMapping(path="/create")
    public Resource setResource(@RequestBody Resource requestResource){
        return resourceRepository.save(requestResource);
    }
}
