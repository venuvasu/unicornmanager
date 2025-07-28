package com.unicorn.manager.repository;

import com.unicorn.manager.model.Unicorn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UnicornRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnicornRepository unicornRepository;

    @Test
    void findByColor_ShouldReturnUnicornsWithMatchingColor() {
        // Arrange
        Unicorn pinkUnicorn = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn blueUnicorn = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1500.00"), LocalDate.of(2022, 3, 15));
        Unicorn anotherPinkUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", new BigDecimal("2000.00"), LocalDate.of(2018, 6, 20));
        
        entityManager.persist(pinkUnicorn);
        entityManager.persist(blueUnicorn);
        entityManager.persist(anotherPinkUnicorn);
        entityManager.flush();

        // Act
        List<Unicorn> pinkUnicorns = unicornRepository.findByColor("Pink");

        // Assert
        assertEquals(2, pinkUnicorns.size());
        assertTrue(pinkUnicorns.stream().allMatch(u -> "Pink".equals(u.getColor())));
    }

    @Test
    void findByAgeLessThan_ShouldReturnUnicornsYoungerThanSpecifiedAge() {
        // Arrange
        Unicorn youngUnicorn1 = new Unicorn(null, "Sparkles", "Pink", 2, "Rainbow Generation", new BigDecimal("800.00"), LocalDate.of(2023, 1, 1));
        Unicorn youngUnicorn2 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1200.00"), LocalDate.of(2022, 3, 15));
        Unicorn olderUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", new BigDecimal("2500.00"), LocalDate.of(2018, 6, 20));
        
        entityManager.persist(youngUnicorn1);
        entityManager.persist(youngUnicorn2);
        entityManager.persist(olderUnicorn);
        entityManager.flush();

        // Act
        List<Unicorn> youngUnicorns = unicornRepository.findByAgeLessThan(5);

        // Assert
        assertEquals(2, youngUnicorns.size());
        assertTrue(youngUnicorns.stream().allMatch(u -> u.getAge() < 5));
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnUnicornsWithMatchingNameFragment() {
        // Arrange
        Unicorn unicorn1 = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn unicorn2 = new Unicorn(null, "Stardust", "Purple", 4, "Flying", new BigDecimal("1800.00"), LocalDate.of(2021, 5, 10));
        Unicorn unicorn3 = new Unicorn(null, "Moonbeam", "Silver", 6, "Dream Walking", new BigDecimal("2200.00"), LocalDate.of(2019, 8, 22));
        
        entityManager.persist(unicorn1);
        entityManager.persist(unicorn2);
        entityManager.persist(unicorn3);
        entityManager.flush();

        // Act
        List<Unicorn> starUnicorns = unicornRepository.findByNameContainingIgnoreCase("star");

        // Assert
        assertEquals(1, starUnicorns.size());
        assertTrue(starUnicorns.stream().anyMatch(u -> u.getName().equalsIgnoreCase("Stardust")));
    }

    @Test
    void findByPrice_ShouldReturnUnicornsWithExactPrice() {
        // Arrange
        BigDecimal targetPrice = new BigDecimal("1500.00");
        Unicorn unicorn1 = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn unicorn2 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", targetPrice, LocalDate.of(2022, 3, 15));
        Unicorn unicorn3 = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", targetPrice, LocalDate.of(2018, 6, 20));
        
        entityManager.persist(unicorn1);
        entityManager.persist(unicorn2);
        entityManager.persist(unicorn3);
        entityManager.flush();

        // Act
        List<Unicorn> unicornsWithTargetPrice = unicornRepository.findByPrice(targetPrice);

        // Assert
        assertEquals(2, unicornsWithTargetPrice.size());
        assertTrue(unicornsWithTargetPrice.stream().allMatch(u -> u.getPrice().equals(targetPrice)));
    }

    @Test
    void findByPriceLessThan_ShouldReturnUnicornsWithPriceLessThanSpecified() {
        // Arrange
        BigDecimal priceLimit = new BigDecimal("1500.00");
        Unicorn cheapUnicorn1 = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn cheapUnicorn2 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1200.00"), LocalDate.of(2022, 3, 15));
        Unicorn expensiveUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", new BigDecimal("2000.00"), LocalDate.of(2018, 6, 20));
        
        entityManager.persist(cheapUnicorn1);
        entityManager.persist(cheapUnicorn2);
        entityManager.persist(expensiveUnicorn);
        entityManager.flush();

        // Act
        List<Unicorn> cheapUnicorns = unicornRepository.findByPriceLessThan(priceLimit);

        // Assert
        assertEquals(2, cheapUnicorns.size());
        assertTrue(cheapUnicorns.stream().allMatch(u -> u.getPrice().compareTo(priceLimit) < 0));
    }

    @Test
    void findByPriceGreaterThan_ShouldReturnUnicornsWithPriceGreaterThanSpecified() {
        // Arrange
        BigDecimal priceThreshold = new BigDecimal("1500.00");
        Unicorn cheapUnicorn = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn expensiveUnicorn1 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1800.00"), LocalDate.of(2022, 3, 15));
        Unicorn expensiveUnicorn2 = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", new BigDecimal("2000.00"), LocalDate.of(2018, 6, 20));
        
        entityManager.persist(cheapUnicorn);
        entityManager.persist(expensiveUnicorn1);
        entityManager.persist(expensiveUnicorn2);
        entityManager.flush();

        // Act
        List<Unicorn> expensiveUnicorns = unicornRepository.findByPriceGreaterThan(priceThreshold);

        // Assert
        assertEquals(2, expensiveUnicorns.size());
        assertTrue(expensiveUnicorns.stream().allMatch(u -> u.getPrice().compareTo(priceThreshold) > 0));
    }

    @Test
    void findByPriceBetween_ShouldReturnUnicornsWithPriceInRange() {
        // Arrange
        BigDecimal minPrice = new BigDecimal("1200.00");
        BigDecimal maxPrice = new BigDecimal("1800.00");
        Unicorn cheapUnicorn = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", new BigDecimal("1000.00"), LocalDate.of(2020, 1, 1));
        Unicorn midPriceUnicorn1 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1500.00"), LocalDate.of(2022, 3, 15));
        Unicorn midPriceUnicorn2 = new Unicorn(null, "Shimmer", "Gold", 4, "Time Travel", new BigDecimal("1600.00"), LocalDate.of(2021, 7, 10));
        Unicorn expensiveUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", new BigDecimal("2000.00"), LocalDate.of(2018, 6, 20));
        
        entityManager.persist(cheapUnicorn);
        entityManager.persist(midPriceUnicorn1);
        entityManager.persist(midPriceUnicorn2);
        entityManager.persist(expensiveUnicorn);
        entityManager.flush();

        // Act
        List<Unicorn> midPriceUnicorns = unicornRepository.findByPriceBetween(minPrice, maxPrice);

        // Assert
        assertEquals(2, midPriceUnicorns.size());
        assertTrue(midPriceUnicorns.stream().allMatch(u -> 
            u.getPrice().compareTo(minPrice) >= 0 && u.getPrice().compareTo(maxPrice) <= 0));
    }
}
