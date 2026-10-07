package com.calzadosmorales.controller.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calzadosmorales.entity.Cliente;
import com.calzadosmorales.entity.PersonaNatural;
import com.calzadosmorales.entity.PersonaJuridica;
import com.calzadosmorales.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteApiController {

    @Autowired
    private ClienteService clienteService;


    @PostMapping(value = "/natural", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registrarPersonaNatural(@RequestBody PersonaNatural persona) {
        try {
            clienteService.guardarPersonaNatural(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(persona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    @PostMapping(value = "/juridica", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registrarPersonaJuridica(@RequestBody PersonaJuridica persona) {
        try {
            clienteService.guardarPersonaJuridica(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(persona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    @GetMapping(value = "/dni/{dni}", produces = "application/json")
    public ResponseEntity<?> buscarPorDni(@PathVariable("dni") String dni) {
        PersonaNatural encontrado = clienteService.buscarPorDni(dni);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"mensaje\": \"Cliente no encontrado con DNI: " + dni + "\"}");
        }
        return ResponseEntity.ok(encontrado);
    }


    @GetMapping(value = "/ruc/{ruc}", produces = "application/json")
    public ResponseEntity<?> buscarPorRuc(@PathVariable("ruc") String ruc) {
        PersonaJuridica encontrado = clienteService.buscarPorRuc(ruc);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"mensaje\": \"Empresa no encontrada con RUC: " + ruc + "\"}");
        }
        return ResponseEntity.ok(encontrado);
    }


    @GetMapping(value = "/listar", produces = "application/json")
    public ResponseEntity<List<Cliente>> listarClientesParaMovil() {
        try {
            List<Cliente> lista = clienteService.listarTodos();
            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}