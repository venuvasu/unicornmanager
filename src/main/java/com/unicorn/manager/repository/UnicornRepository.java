package com.unicorn.manager.repository;

import com.unicorn.manager.model.Unicorn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UnicornRepository extends JpaRepository<Unicorn, Long> {
    
    List<Unicorn> findByColor(String color);
    
    List<Unicorn> findByAgeLessThan(int age);
    
    List<Unicorn> findByNameContainingIgnoreCase(String nameFragment);
    
    List<Unicorn> findByPrice(BigDecimal price);
    
    List<Unicorn> findByPriceLessThan(BigDecimal price);
    
    List<Unicorn> findByPriceGreaterThan(BigDecimal price);
    
    List<Unicorn> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
