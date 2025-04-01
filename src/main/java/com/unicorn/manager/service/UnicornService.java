package com.unicorn.manager.service;

import com.unicorn.manager.model.Unicorn;
import com.unicorn.manager.repository.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UnicornService {

    private final UnicornRepository unicornRepository;

    @Autowired
    public UnicornService(UnicornRepository unicornRepository) {
        this.unicornRepository = unicornRepository;
    }

    public List<Unicorn> getAllUnicorns() {
        return unicornRepository.findAll();
    }

    public Unicorn getUnicornById(Long id) {
        return unicornRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unicorn not found with id: " + id));
    }

    public Unicorn createUnicorn(Unicorn unicorn) {
        return unicornRepository.save(unicorn);
    }

    public Unicorn updateUnicorn(Long id, Unicorn unicornDetails) {
        Unicorn unicorn = getUnicornById(id);
        
        unicorn.setName(unicornDetails.getName());
        unicorn.setColor(unicornDetails.getColor());
        unicorn.setAge(unicornDetails.getAge());
        unicorn.setMagicalAbility(unicornDetails.getMagicalAbility());
        unicorn.setBirthDate(unicornDetails.getBirthDate());
        
        return unicornRepository.save(unicorn);
    }

    public void deleteUnicorn(Long id) {
        Unicorn unicorn = getUnicornById(id);
        unicornRepository.delete(unicorn);
    }

    public List<Unicorn> findUnicornsByColor(String color) {
        return unicornRepository.findByColor(color);
    }

    public List<Unicorn> findUnicornsByAgeLessThan(int age) {
        return unicornRepository.findByAgeLessThan(age);
    }

    public List<Unicorn> findUnicornsByNameContaining(String nameFragment) {
        return unicornRepository.findByNameContainingIgnoreCase(nameFragment);
    }
}
