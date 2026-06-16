package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.RevisionCalidad;
import vallegrande.edu.pe.arona.model.RevisionCalidadDTO;
import vallegrande.edu.pe.arona.service.RevisionCalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/revisiones-calidad")
@CrossOrigin("*")
@Tag(name = "RevisionCalidad-Controller", description = "HU6: Revisión de calidad antes del despacho")
public class RevisionCalidadController {

    @Autowired
    private RevisionCalidadService service;

    @GetMapping
    @Operation(summary = "Listar todas las revisiones")
    public ResponseEntity<List<RevisionCalidad>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<RevisionCalidad> listarPorId(@PathVariable Long id) {
        return service.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cosecha/{idCosecha}")
    @Operation(summary = "Revisiones por cosecha")
    public ResponseEntity<List<RevisionCalidad>> listarPorCosecha(@PathVariable Long idCosecha) {
        return ResponseEntity.ok(service.listarPorCosecha(idCosecha));
    }

    @PostMapping
    @Operation(summary = "Registrar revisión", description = "Registra una revisión usando IDs de cosecha y supervisor. Si no cumple requisitos, se notifica automáticamente")
    public ResponseEntity<RevisionCalidad> crear(@RequestBody RevisionCalidadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar revisión", description = "Edita una revisión usando IDs de cosecha y supervisor")
    public ResponseEntity<RevisionCalidad> editar(@PathVariable Long id, @RequestBody RevisionCalidadDTO dto) {
        RevisionCalidad editada = service.editar(id, dto);
        return ResponseEntity.ok(editada);
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (lógico)")
    public ResponseEntity<RevisionCalidad> eliminar(@PathVariable Long id) {
        RevisionCalidad eliminada = service.eliminar(id);
        return (eliminada != null) ? ResponseEntity.ok(eliminada) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar revisión")
    public ResponseEntity<RevisionCalidad> restaurar(@PathVariable Long id) {
        RevisionCalidad restaurada = service.restaurar(id);
        return (restaurada != null) ? ResponseEntity.ok(restaurada) : ResponseEntity.notFound().build();
    }
}
