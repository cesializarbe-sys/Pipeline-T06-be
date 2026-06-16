package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.ActividadCampo;
import vallegrande.edu.pe.arona.service.ActividadCampoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/actividades-campo")
@CrossOrigin("*")
@Tag(name = "ActividadCampo-Controller", description = "HU2: Anotación de trabajos en campo")
public class ActividadCampoController {

    @Autowired
    private ActividadCampoService service;

    @GetMapping
    @Operation(summary = "Listar todas las actividades")
    public ResponseEntity<List<ActividadCampo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<ActividadCampo> listarPorId(@PathVariable Long id) {
        return service.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/campo/{idCampo}")
    @Operation(summary = "Filtrar por campo")
    public ResponseEntity<List<ActividadCampo>> listarPorCampo(@PathVariable Long idCampo) {
        return ResponseEntity.ok(service.listarPorCampo(idCampo));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Mis actividades", description = "Actividades registradas por un trabajador")
    public ResponseEntity<List<ActividadCampo>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    @PostMapping
    @Operation(summary = "Registrar actividad de campo")
    public ResponseEntity<ActividadCampo> crear(@RequestBody ActividadCampo actividad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(actividad));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar actividad")
    public ResponseEntity<ActividadCampo> editar(@PathVariable Long id, @RequestBody ActividadCampo actividad) {
        ActividadCampo editada = service.editar(id, actividad);
        return ResponseEntity.ok(editada);
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (lógico)")
    public ResponseEntity<ActividadCampo> eliminar(@PathVariable Long id) {
        ActividadCampo eliminada = service.eliminar(id);
        return (eliminada != null) ? ResponseEntity.ok(eliminada) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar")
    public ResponseEntity<ActividadCampo> restaurar(@PathVariable Long id) {
        ActividadCampo restaurada = service.restaurar(id);
        return (restaurada != null) ? ResponseEntity.ok(restaurada) : ResponseEntity.notFound().build();
    }
}

