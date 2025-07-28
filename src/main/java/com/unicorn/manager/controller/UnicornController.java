package com.unicorn.manager.controller;

import com.unicorn.manager.model.Unicorn;
import com.unicorn.manager.service.UnicornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/unicorns")
public class UnicornController {

    private final UnicornService unicornService;

    @Autowired
    public UnicornController(UnicornService unicornService) {
        this.unicornService = unicornService;
    }

    @GetMapping
    public ResponseEntity<List<Unicorn>> getAllUnicorns() {
        List<Unicorn> unicorns = unicornService.getAllUnicorns();
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unicorn> getUnicornById(@PathVariable Long id) {
        Unicorn unicorn = unicornService.getUnicornById(id);
        return new ResponseEntity<>(unicorn, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Unicorn> createUnicorn(@Valid @RequestBody Unicorn unicorn) {
        Unicorn newUnicorn = unicornService.createUnicorn(unicorn);
        return new ResponseEntity<>(newUnicorn, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unicorn> updateUnicorn(@PathVariable Long id, @Valid @RequestBody Unicorn unicorn) {
        Unicorn updatedUnicorn = unicornService.updateUnicorn(id, unicorn);
        return new ResponseEntity<>(updatedUnicorn, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnicorn(@PathVariable Long id) {
        unicornService.deleteUnicorn(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Unicorn>> getUnicornsByColor(@PathVariable String color) {
        List<Unicorn> unicorns = unicornService.findUnicornsByColor(color);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/young/{age}")
    public ResponseEntity<List<Unicorn>> getYoungUnicorns(@PathVariable int age) {
        List<Unicorn> unicorns = unicornService.findUnicornsByAgeLessThan(age);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Unicorn>> searchUnicorns(@RequestParam String name) {
        List<Unicorn> unicorns = unicornService.findUnicornsByNameContaining(name);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<Unicorn>> getUnicornsByPrice(@PathVariable BigDecimal price) {
        List<Unicorn> unicorns = unicornService.findUnicornsByPrice(price);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/price/less-than/{price}")
    public ResponseEntity<List<Unicorn>> getUnicornsByPriceLessThan(@PathVariable BigDecimal price) {
        List<Unicorn> unicorns = unicornService.findUnicornsByPriceLessThan(price);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/price/greater-than/{price}")
    public ResponseEntity<List<Unicorn>> getUnicornsByPriceGreaterThan(@PathVariable BigDecimal price) {
        List<Unicorn> unicorns = unicornService.findUnicornsByPriceGreaterThan(price);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }

    @GetMapping("/price/range")
    public ResponseEntity<List<Unicorn>> getUnicornsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<Unicorn> unicorns = unicornService.findUnicornsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(unicorns, HttpStatus.OK);
    }
}
