package com.unicorn.manager.service;

import com.unicorn.manager.model.Unicorn;
import com.unicorn.manager.repository.UnicornRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnicornServiceTest {

    @Mock
    private UnicornRepository unicornRepository;

    @InjectMocks
    private UnicornService unicornService;

    private Unicorn unicorn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        unicorn = new Unicorn();
        unicorn.setId(1L);
        unicorn.setName("Sparkles");
        unicorn.setColor("Pink");
        unicorn.setAge(5);
        unicorn.setMagicalAbility("Rainbow Generation");
        unicorn.setBirthDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    void getAllUnicorns_ShouldReturnAllUnicorns() {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn, new Unicorn());
        when(unicornRepository.findAll()).thenReturn(unicorns);

        // Act
        List<Unicorn> result = unicornService.getAllUnicorns();

        // Assert
        assertEquals(2, result.size());
        verify(unicornRepository, times(1)).findAll();
    }

    @Test
    void getUnicornById_WithValidId_ShouldReturnUnicorn() {
        // Arrange
        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));

        // Act
        Unicorn result = unicornService.getUnicornById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Sparkles", result.getName());
        verify(unicornRepository, times(1)).findById(1L);
    }

    @Test
    void getUnicornById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(unicornRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            unicornService.getUnicornById(99L);
        });
        verify(unicornRepository, times(1)).findById(99L);
    }

    @Test
    void createUnicorn_ShouldReturnSavedUnicorn() {
        // Arrange
        when(unicornRepository.save(any(Unicorn.class))).thenReturn(unicorn);

        // Act
        Unicorn result = unicornService.createUnicorn(unicorn);

        // Assert
        assertNotNull(result);
        assertEquals("Sparkles", result.getName());
        verify(unicornRepository, times(1)).save(unicorn);
    }

    @Test
    void updateUnicorn_WithValidId_ShouldReturnUpdatedUnicorn() {
        // Arrange
        Unicorn updatedUnicorn = new Unicorn();
        updatedUnicorn.setName("Glitter");
        updatedUnicorn.setColor("Purple");
        updatedUnicorn.setAge(6);
        updatedUnicorn.setMagicalAbility("Starlight Creation");
        updatedUnicorn.setBirthDate(LocalDate.of(2019, 5, 10));

        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));
        when(unicornRepository.save(any(Unicorn.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Unicorn result = unicornService.updateUnicorn(1L, updatedUnicorn);

        // Assert
        assertNotNull(result);
        assertEquals("Glitter", result.getName());
        assertEquals("Purple", result.getColor());
        assertEquals(6, result.getAge());
        assertEquals("Starlight Creation", result.getMagicalAbility());
        assertEquals(LocalDate.of(2019, 5, 10), result.getBirthDate());
        verify(unicornRepository, times(1)).findById(1L);
        verify(unicornRepository, times(1)).save(any(Unicorn.class));
    }

    @Test
    void deleteUnicorn_WithValidId_ShouldDeleteUnicorn() {
        // Arrange
        when(unicornRepository.findById(1L)).thenReturn(Optional.of(unicorn));
        doNothing().when(unicornRepository).delete(unicorn);

        // Act
        unicornService.deleteUnicorn(1L);

        // Assert
        verify(unicornRepository, times(1)).findById(1L);
        verify(unicornRepository, times(1)).delete(unicorn);
    }

    @Test
    void findUnicornsByColor_ShouldReturnMatchingUnicorns() {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornRepository.findByColor("Pink")).thenReturn(unicorns);

        // Act
        List<Unicorn> result = unicornService.findUnicornsByColor("Pink");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Pink", result.get(0).getColor());
        verify(unicornRepository, times(1)).findByColor("Pink");
    }

    @Test
    void findUnicornsByAgeLessThan_ShouldReturnMatchingUnicorns() {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornRepository.findByAgeLessThan(10)).thenReturn(unicorns);

        // Act
        List<Unicorn> result = unicornService.findUnicornsByAgeLessThan(10);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAge() < 10);
        verify(unicornRepository, times(1)).findByAgeLessThan(10);
    }

    @Test
    void findUnicornsByNameContaining_ShouldReturnMatchingUnicorns() {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornRepository.findByNameContainingIgnoreCase("Spark")).thenReturn(unicorns);

        // Act
        List<Unicorn> result = unicornService.findUnicornsByNameContaining("Spark");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Spark"));
        verify(unicornRepository, times(1)).findByNameContainingIgnoreCase("Spark");
    }
}
