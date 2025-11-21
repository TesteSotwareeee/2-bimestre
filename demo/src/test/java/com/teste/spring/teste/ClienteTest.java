package com.teste.spring.teste.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveSetarEObterValores() {
        Cliente c = new Cliente();

        LocalDateTime agora = LocalDateTime.now();

        c.setId(10L);
        c.setNome("Nome Teste");
        c.setEmail("email@teste.com");
        c.setTelefone("12345");
        c.setCriadoEm(agora);

        assertEquals(10L, c.getId());
        assertEquals("Nome Teste", c.getNome());
        assertEquals("email@teste.com", c.getEmail());
        assertEquals("12345", c.getTelefone());
        assertEquals(agora, c.getCriadoEm());
    }

    @Test
    void prePersist_deveGerarCriadoEmQuandoNulo() {
        Cliente c = new Cliente();

        assertNull(c.getCriadoEm());

        c.prePersist();

        assertNotNull(c.getCriadoEm());
    }
}
