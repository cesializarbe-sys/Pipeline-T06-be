package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.Clasificacion;
import vallegrande.edu.pe.arona.service.ClasificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clasificaciones")
@CrossOrigin("*")
@Tag(name = "Clasificacion-Controller", description = "HU4: Separación por tamaño y calidad")
public class ClasificacionController {

    @Autowired
    private ClasificacionService service;

    @GetMapping
    @Operation(summary = "Listar todas las clasificaciones")
    public ResponseEntity<List<Clasificacion>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<Clasificacion> listarPorId(@PathVariable Long id) {
        return service.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cosecha/{idCosecha}")
    @Operation(summary = "Clasificaciones por cosecha")
    public ResponseEntity<List<Clasificacion>> listarPorCosecha(@PathVariable Long idCosecha) {
        return ResponseEntity.ok(service.listarPorCosecha(idCosecha));
    }

    @GetMapping("/resumen/{idCosecha}")
    @Operation(summary = "Resumen por tipo", description = "Resumen clasificado por calibre para una cosecha")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen(@PathVariable Long idCosecha) {
        return ResponseEntity.ok(service.obtenerResumenPorCosecha(idCosecha));
    }

    @GetMapping("/aptos-exportacion")
    @Operation(summary = "Aptos para exportación", description = "Clasificaciones marcadas como aptas")
    public ResponseEntity<List<Clasificacion>> listarAptos() {
        return ResponseEntity.ok(service.listarAptosExportacion());
    }

    @PostMapping
    @Operation(summary = "Registrar clasificación")
    public ResponseEntity<Clasificacion> crear(@RequestBody Clasificacion clasificacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(clasificacion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar clasificación")
    public ResponseEntity<Clasificacion> editar(@PathVariable Long id, @RequestBody Clasificacion clasificacion) {
        return ResponseEntity.ok(service.editar(id, clasificacion));
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar clasificación (lógico)", description = "Cambia el estado a inactivo y registra deletedAt")
    public ResponseEntity<Clasificacion> eliminar(@PathVariable Long id) {
        Clasificacion eliminada = service.eliminar(id);
        return (eliminada != null) ? ResponseEntity.ok(eliminada) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar clasificación", description = "Reactiva una clasificación eliminada lógicamente")
    public ResponseEntity<Clasificacion> restaurar(@PathVariable Long id) {
        Clasificacion restaurada = service.restaurar(id);
        return (restaurada != null) ? ResponseEntity.ok(restaurada) : ResponseEntity.notFound().build();
    }
}

