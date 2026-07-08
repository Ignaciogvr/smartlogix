package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.model.DireccionUsuario;
import com.smartlogix.usuarios.service.PerfilUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioId}/direcciones")
public class DireccionController {
    
    private final PerfilUsuarioService service;

    public DireccionController(PerfilUsuarioService service) {
        this.service = service;
    }
    
    // POST /usuarios/{usuarioId}/direcciones
    @PostMapping
    public ResponseEntity<DireccionUsuario> agregar(@PathVariable Long usuarioId, 
                                                      @RequestBody DireccionUsuario direccion) {
        return ResponseEntity.ok(service.addDireccion(usuarioId, direccion));
    }
    
    // GET /usuarios/{usuarioId}/direcciones
    @GetMapping
    public ResponseEntity<List<DireccionUsuario>> listar(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.getDireccionesByUsuario(usuarioId));
    }
    
    // PUT /usuarios/{usuarioId}/direcciones/{id}
    @PutMapping("/{id}")
    public ResponseEntity<DireccionUsuario> actualizar(@PathVariable Long usuarioId,
                                                         @PathVariable Long id,
                                                         @RequestBody DireccionUsuario direccion) {
        return ResponseEntity.ok(service.updateDireccion(id, direccion));
    }
    
    // DELETE /usuarios/{usuarioId}/direcciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long usuarioId, @PathVariable Long id) {
        service.deleteDireccion(id);
        return ResponseEntity.ok().build();
    }
    
    // PUT /usuarios/{usuarioId}/direcciones/{id}/predeterminada
    @PutMapping("/{id}/predeterminada")
    public ResponseEntity<Void> establecerPredeterminada(@PathVariable Long usuarioId,
                                                           @PathVariable Long id) {
        service.setDireccionPredeterminada(usuarioId, id);
        return ResponseEntity.ok().build();
    }
}
