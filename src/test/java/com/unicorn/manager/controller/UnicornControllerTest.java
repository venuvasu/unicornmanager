package com.unicorn.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.unicorn.manager.model.Unicorn;
import com.unicorn.manager.service.UnicornService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnicornController.class)
class UnicornControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnicornService unicornService;

    private Unicorn unicorn;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        unicorn = new Unicorn();
        unicorn.setId(1L);
        unicorn.setName("Sparkles");
        unicorn.setColor("Pink");
        unicorn.setAge(5);
        unicorn.setMagicalAbility("Rainbow Generation");
        unicorn.setPrice(new BigDecimal("1500.00"));
        unicorn.setBirthDate(LocalDate.of(2020, 1, 1));

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllUnicorns_ShouldReturnAllUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn, new Unicorn(2L, "Glimmer", "Blue", 3, "Teleportation", new BigDecimal("1200.00"), LocalDate.of(2022, 3, 15)));
        when(unicornService.getAllUnicorns()).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Sparkles")))
                .andExpect(jsonPath("$[1].name", is("Glimmer")));

        verify(unicornService, times(1)).getAllUnicorns();
    }

    @Test
    void getUnicornById_WithValidId_ShouldReturnUnicorn() throws Exception {
        // Arrange
        when(unicornService.getUnicornById(1L)).thenReturn(unicorn);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sparkles")))
                .andExpect(jsonPath("$.color", is("Pink")))
                .andExpect(jsonPath("$.age", is(5)))
                .andExpect(jsonPath("$.magicalAbility", is("Rainbow Generation")));

        verify(unicornService, times(1)).getUnicornById(1L);
    }

    @Test
    void getUnicornById_WithInvalidId_ShouldReturn404() throws Exception {
        // Arrange
        when(unicornService.getUnicornById(99L)).thenThrow(new EntityNotFoundException("Unicorn not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/99"))
                .andExpect(status().isNotFound());

        verify(unicornService, times(1)).getUnicornById(99L);
    }

    @Test
    void createUnicorn_WithValidData_ShouldReturnCreatedUnicorn() throws Exception {
        // Arrange
        when(unicornService.createUnicorn(any(Unicorn.class))).thenReturn(unicorn);

        // Act & Assert
        mockMvc.perform(post("/api/unicorns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unicorn)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Sparkles")))
                .andExpect(jsonPath("$.color", is("Pink")));

        verify(unicornService, times(1)).createUnicorn(any(Unicorn.class));
    }

    @Test
    void updateUnicorn_WithValidData_ShouldReturnUpdatedUnicorn() throws Exception {
        // Arrange
        Unicorn updatedUnicorn = new Unicorn(1L, "Glitter", "Purple", 6, "Starlight Creation", new BigDecimal("2000.00"), LocalDate.of(2019, 5, 10));
        when(unicornService.updateUnicorn(eq(1L), any(Unicorn.class))).thenReturn(updatedUnicorn);

        // Act & Assert
        mockMvc.perform(put("/api/unicorns/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUnicorn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Glitter")))
                .andExpect(jsonPath("$.color", is("Purple")))
                .andExpect(jsonPath("$.age", is(6)))
                .andExpect(jsonPath("$.magicalAbility", is("Starlight Creation")));

        verify(unicornService, times(1)).updateUnicorn(eq(1L), any(Unicorn.class));
    }

    @Test
    void deleteUnicorn_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(unicornService).deleteUnicorn(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/unicorns/1"))
                .andExpect(status().isNoContent());

        verify(unicornService, times(1)).deleteUnicorn(1L);
    }

    @Test
    void getUnicornsByColor_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByColor("Pink")).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/color/Pink"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].color", is("Pink")));

        verify(unicornService, times(1)).findUnicornsByColor("Pink");
    }

    @Test
    void getYoungUnicorns_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByAgeLessThan(10)).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/young/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].age", lessThan(10)));

        verify(unicornService, times(1)).findUnicornsByAgeLessThan(10);
    }

    @Test
    void searchUnicorns_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByNameContaining("Spark")).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/search").param("name", "Spark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", containsString("Spark")));

        verify(unicornService, times(1)).findUnicornsByNameContaining("Spark");
    }

    @Test
    void getUnicornsByPrice_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByPrice(new BigDecimal("1500.00"))).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/price/1500.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", is(1500.00)));

        verify(unicornService, times(1)).findUnicornsByPrice(new BigDecimal("1500.00"));
    }

    @Test
    void getUnicornsByPriceLessThan_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByPriceLessThan(new BigDecimal("2000.00"))).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/price/less-than/2000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", lessThan(2000.00)));

        verify(unicornService, times(1)).findUnicornsByPriceLessThan(new BigDecimal("2000.00"));
    }

    @Test
    void getUnicornsByPriceGreaterThan_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByPriceGreaterThan(new BigDecimal("1000.00"))).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/price/greater-than/1000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", greaterThan(1000.00)));

        verify(unicornService, times(1)).findUnicornsByPriceGreaterThan(new BigDecimal("1000.00"));
    }

    @Test
    void getUnicornsByPriceRange_ShouldReturnMatchingUnicorns() throws Exception {
        // Arrange
        List<Unicorn> unicorns = Arrays.asList(unicorn);
        when(unicornService.findUnicornsByPriceRange(new BigDecimal("1000.00"), new BigDecimal("2000.00"))).thenReturn(unicorns);

        // Act & Assert
        mockMvc.perform(get("/api/unicorns/price/range")
                .param("minPrice", "1000.00")
                .param("maxPrice", "2000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", allOf(greaterThanOrEqualTo(1000.00), lessThanOrEqualTo(2000.00))));

        verify(unicornService, times(1)).findUnicornsByPriceRange(new BigDecimal("1000.00"), new BigDecimal("2000.00"));
    }
}
