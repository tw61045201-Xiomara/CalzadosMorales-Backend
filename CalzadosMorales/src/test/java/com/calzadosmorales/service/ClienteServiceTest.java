package com.calzadosmorales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.calzadosmorales.entity.PersonaNatural;
import com.calzadosmorales.repository.ClienteRepository;
import com.calzadosmorales.repository.PersonaJuridicaRepository;
import com.calzadosmorales.repository.PersonaNaturalRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaNaturalRepository personaNaturalRepository;

    @Mock
    private PersonaJuridicaRepository personaJuridicaRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("CA004-1: Registrar cliente nuevo con DNI válido")
    void testGuardarPersonaNaturalValida() {
        PersonaNatural persona = new PersonaNatural();
        persona.setDni("72458912");

        when(personaNaturalRepository.save(any(PersonaNatural.class))).thenReturn(persona);

        clienteService.guardarPersonaNatural(persona);

        verify(personaNaturalRepository, times(1)).save(persona);
    }

    @Test
    @DisplayName("CA004-2: Detección de cliente existente por DNI")
    void testValidarDniExistente() {
        PersonaNatural personaExistente = new PersonaNatural();
        personaExistente.setDni("72458912");

        when(personaNaturalRepository.findByDni("72458912")).thenReturn(personaExistente);

        PersonaNatural resultado = clienteService.buscarPorDni("72458912");

        assertNotNull(resultado);
        assertEquals("72458912", resultado.getDni());
        verify(personaNaturalRepository, times(1)).findByDni("72458912");
    }

    @Test
    @DisplayName("CA004-3: Buscar cliente por número de documento")
    void testBuscarPorDni() {
        PersonaNatural persona = new PersonaNatural();
        persona.setDni("72458912");

        when(personaNaturalRepository.findByDni("72458912")).thenReturn(persona);

        PersonaNatural resultado = clienteService.buscarPorDni("72458912");

        assertNotNull(resultado);
        assertEquals("72458912", resultado.getDni());
        verify(personaNaturalRepository, times(1)).findByDni("72458912");
    }
}