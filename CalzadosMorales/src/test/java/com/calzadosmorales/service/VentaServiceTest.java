package com.calzadosmorales.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.calzadosmorales.entity.DetalleVenta;
import com.calzadosmorales.entity.ProductoTalla;
import com.calzadosmorales.entity.Venta;
import com.calzadosmorales.repository.ProductoTallaRepository;
import com.calzadosmorales.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoTallaRepository productoTallaRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VentaService ventaService;

    @Test
    @DisplayName("CA003-1: Registrar venta con stock suficiente y verificar rebaja")
    void registrarVenta_StockSuficiente_RetornaVentaGuardada() {
       
        ProductoTalla pt = new ProductoTalla();
        pt.setStock(10); 

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProductoTalla(pt);
        detalle.setCantidad(2); 

        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(detalle);

        Venta venta = new Venta();
        venta.setDetalles(detalles);

        when(productoTallaRepository.save(any(ProductoTalla.class))).thenReturn(pt);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

      
        Venta resultado = ventaService.registrarVenta(venta);

        
        assertNotNull(resultado);
        assertEquals(8, pt.getStock(), "El stock debe rebajar de 10 a 8");
        verify(productoTallaRepository, times(1)).save(pt);
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    @DisplayName("CA003-2: Rechazar venta cuando el stock es insuficiente")
    void registrarVenta_StockInsuficiente_LanzaExcepcion() {
        
        ProductoTalla pt = new ProductoTalla();
        pt.setStock(2); 

        DetalleVenta detalle = new DetalleVenta();
        detalle.setProductoTalla(pt);
        detalle.setCantidad(5); 

        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(detalle);

        Venta venta = new Venta();
        venta.setDetalles(detalles);

        // 2. Act & Assert (Debe lanzar RuntimeException por falta de stock)
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            ventaService.registrarVenta(venta);
        });

        assertTrue(excepcion.getMessage().contains("Stock insuficiente"));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    @DisplayName("CA003-3: Buscar venta por ID existente")
    void buscarPorId_Existente_RetornaVenta() {
        Venta ventaMock = new Venta();
        when(ventaRepository.findById(1)).thenReturn(Optional.of(ventaMock));

        Venta resultado = ventaService.buscarPorId(1);

        assertNotNull(resultado);
        verify(ventaRepository, times(1)).findById(1);
    }
}