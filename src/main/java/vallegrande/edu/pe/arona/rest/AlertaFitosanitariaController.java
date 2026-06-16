package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.AlertaFitosanitaria;
import vallegrande.edu.pe.arona.dto.AlertaFitosanitariaDTO;
import vallegrande.edu.pe.arona.model.ErrorResponse;
import vallegrande.edu.pe.arona.service.AlertaFitosanitariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin("*")
@Tag(name = "AlertaFitosanitaria-Controller", description = "HU7: Aviso de plagas o enfermedades")
public class AlertaFitosanitariaController {

    @Autowired
    private AlertaFitosanitariaService service;

    @GetMapping
    @Operation(summary = "Listar todas las alertas")
    public ResponseEntity<List<AlertaFitosanitaria>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        return service.listarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Alerta no encontrada con ID: " + id)));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Alertas pendientes", description = "Solo alertas que no han sido atendidas")
    public ResponseEntity<List<AlertaFitosanitaria>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @GetMapping("/campo/{idCampo}")
    @Operation(summary = "Alertas por campo")
    public ResponseEntity<List<AlertaFitosanitaria>> listarPorCampo(@PathVariable Long idCampo) {
        return ResponseEntity.ok(service.listarPorCampo(idCampo));
    }

    @PostMapping
    @Operation(summary = "Crear alerta fitosanitaria",
            description = "Registra una alerta usando IDs de campo, cultivo y usuario")
    public ResponseEntity<?> crear(@Valid @RequestBody AlertaFitosanitariaDTO dto) {
        try {
            AlertaFitosanitaria creada = service.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar alerta",
            description = "Edita una alerta usando IDs de campo, cultivo y usuario")
    public ResponseEntity<?> editar(@PathVariable Long id,
                                    @Valid @RequestBody AlertaFitosanitariaDTO dto) {
        try {
            AlertaFitosanitaria editada = service.editar(id, dto);
            return ResponseEntity.ok(editada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/atender")
    @Operation(summary = "Marcar como atendido",
            description = "Registra la solución aplicada y cambia el estado")
    public ResponseEntity<?> atender(@PathVariable Long id,
                                     @RequestBody Map<String, String> body) {
        try {
            String solucion = body.get("solucionAplicada");
            AlertaFitosanitaria atendida = service.atender(id, solucion);
            return ResponseEntity.ok(atendida);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (lógico)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        AlertaFitosanitaria eliminada = service.eliminar(id);
        return (eliminada != null)
                ? ResponseEntity.ok(eliminada)
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Alerta no encontrada con ID: " + id));
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar alerta")
    public ResponseEntity<?> restaurar(@PathVariable Long id) {
        AlertaFitosanitaria restaurada = service.restaurar(id);
        return (restaurada != null)
                ? ResponseEntity.ok(restaurada)
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Alerta no encontrada con ID: " + id));
    }
}
