package com.calzadosmorales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.calzadosmorales.entity.Rol;
import com.calzadosmorales.entity.Usuario;
import com.calzadosmorales.repository.RolRepository;
import com.calzadosmorales.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private RolRepository rolRepo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPrueba;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol();
        rolAdmin.setId_rol(1);

        usuarioPrueba = new Usuario();
        usuarioPrueba.setId_usuario(1);
        usuarioPrueba.setUsuario("admin");
        usuarioPrueba.setClave("$2a$10$encodedPasswordHashMock");
        usuarioPrueba.setEstado(true);
        usuarioPrueba.setRol(rolAdmin);
    }

    @Test
    @DisplayName("CA002-1: Autenticación exitosa de usuario activo (loadUserByUsername)")
    void testLoadUserByUsernameExitoso() {
        when(usuarioRepo.findByUsuario("admin")).thenReturn(usuarioPrueba);

        UserDetails userDetails = usuarioService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("$2a$10$encodedPasswordHashMock", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_1")));
        verify(usuarioRepo, times(1)).findByUsuario("admin");
    }

    @Test
    @DisplayName("CA002-2: Autenticación de usuario inexistente lanza UsernameNotFoundException")
    void testLoadUserByUsernameNoExiste() {
        when(usuarioRepo.findByUsuario("no_existe")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername("no_existe");
        });
        verify(usuarioRepo, times(1)).findByUsuario("no_existe");
    }

    @Test
    @DisplayName("CA002-3: Búsqueda de usuario por ID existente")
    void testBuscarUsuarioPorId() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioPrueba));

        Usuario resultado = usuarioService.buscarUsuarioPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_usuario());
        assertEquals("admin", resultado.getUsuario());
        verify(usuarioRepo, times(1)).findById(1);
    }
}