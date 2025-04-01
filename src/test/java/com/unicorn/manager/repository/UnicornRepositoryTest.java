package com.unicorn.manager.repository;

import com.unicorn.manager.model.Unicorn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
        Unicorn pinkUnicorn = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", LocalDate.of(2020, 1, 1));
        Unicorn blueUnicorn = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", LocalDate.of(2022, 3, 15));
        Unicorn anotherPinkUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", LocalDate.of(2018, 6, 20));
        
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
        Unicorn youngUnicorn1 = new Unicorn(null, "Sparkles", "Pink", 2, "Rainbow Generation", LocalDate.of(2023, 1, 1));
        Unicorn youngUnicorn2 = new Unicorn(null, "Glimmer", "Blue", 3, "Teleportation", LocalDate.of(2022, 3, 15));
        Unicorn olderUnicorn = new Unicorn(null, "Twinkle", "Pink", 7, "Healing", LocalDate.of(2018, 6, 20));
        
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
        Unicorn unicorn1 = new Unicorn(null, "Sparkles", "Pink", 5, "Rainbow Generation", LocalDate.of(2020, 1, 1));
        Unicorn unicorn2 = new Unicorn(null, "Stardust", "Purple", 4, "Flying", LocalDate.of(2021, 5, 10));
        Unicorn unicorn3 = new Unicorn(null, "Moonbeam", "Silver", 6, "Dream Walking", LocalDate.of(2019, 8, 22));
        
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
}
