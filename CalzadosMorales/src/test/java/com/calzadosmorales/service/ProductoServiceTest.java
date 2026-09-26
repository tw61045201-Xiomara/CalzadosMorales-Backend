package com.calzadosmorales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.calzadosmorales.entity.Color;
import com.calzadosmorales.entity.Producto;
import com.calzadosmorales.repository.CategoriaRepository;
import com.calzadosmorales.repository.ColorRepository;
import com.calzadosmorales.repository.MaterialRepository;
import com.calzadosmorales.repository.ProductoRepository;
import com.calzadosmorales.repository.ProductoTallaRepository;
import com.calzadosmorales.repository.TallaRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private CategoriaRepository categoriaRepo;

    @Mock
    private TallaRepository tallaRepo;

    @Mock
    private ColorRepository colorRepo;

    @Mock
    private MaterialRepository materialRepo;

    @Mock
    private ProductoTallaRepository productoTallaRepo;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoPrueba;

    @BeforeEach
    void setUp() {
        productoPrueba = new Producto();
        productoPrueba.setId_producto(1);
        productoPrueba.setNombre("Mocasín Cuero Oxford");
        productoPrueba.setPrecio(new BigDecimal("189.90"));
        productoPrueba.setEstado(true);
    }

    @Test
    @DisplayName("CA001-1: Listar catálogo general de calzado")
    void testListarProductos() {
        when(productoRepo.findAll()).thenReturn(Arrays.asList(productoPrueba));

        List<Producto> resultado = productoService.listarProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mocasín Cuero Oxford", resultado.get(0).getNombre());
        verify(productoRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("CA001-2: Buscar producto por ID existente")
    void testBuscarProductoExistente() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(productoPrueba));

        Producto resultado = productoService.buscarProducto(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_producto());
        assertEquals("Mocasín Cuero Oxford", resultado.getNombre());
        verify(productoRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("CA001-3: Buscar producto inexistente retorna null")
    void testBuscarProductoNoExistente() {
        when(productoRepo.findById(99)).thenReturn(Optional.empty());

        Producto resultado = productoService.buscarProducto(99);

        assertNull(resultado);
        verify(productoRepo, times(1)).findById(99);
    }

    @Test
    @DisplayName("CA001-4: Validar duplicidad de producto por nombre y color")
    void testExisteProductoIgual() {
        Color colorNegro = new Color();
        when(productoRepo.existsByNombreAndColor("Mocasín Cuero Oxford", colorNegro)).thenReturn(true);

        boolean existe = productoService.existeProductoIgual("Mocasín Cuero Oxford", colorNegro);

        assertTrue(existe);
        verify(productoRepo, times(1)).existsByNombreAndColor("Mocasín Cuero Oxford", colorNegro);
    }
}