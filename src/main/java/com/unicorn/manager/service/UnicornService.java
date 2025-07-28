package com.unicorn.manager.service;

import com.unicorn.manager.model.Unicorn;
import com.unicorn.manager.repository.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
        unicorn.setPrice(unicornDetails.getPrice());
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
    
    public List<Unicorn> findUnicornsByPrice(BigDecimal price) {
        return unicornRepository.findByPrice(price);
    }
    
    public List<Unicorn> findUnicornsByPriceLessThan(BigDecimal price) {
        return unicornRepository.findByPriceLessThan(price);
    }
    
    public List<Unicorn> findUnicornsByPriceGreaterThan(BigDecimal price) {
        return unicornRepository.findByPriceGreaterThan(price);
    }
    
    public List<Unicorn> findUnicornsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return unicornRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
