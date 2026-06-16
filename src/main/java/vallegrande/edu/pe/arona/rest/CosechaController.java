package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.dto.CosechaDTO;
import vallegrande.edu.pe.arona.model.Cosecha;
import vallegrande.edu.pe.arona.service.CosechaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el módulo de Cosechas (HU3).
 * Endpoints disponibles:
 *   GET    /api/cosechas              → Listar todas las cosechas
 *   GET    /api/cosechas/{id}         → Buscar cosecha por ID
 *   GET    /api/cosechas/estado/{e}   → Filtrar por estado (true/false)
 *   GET    /api/cosechas/resumen      → Resumen agrupado por cultivo
 *   POST   /api/cosechas              → Registrar cosecha (recibe CosechaDTO)
 *   PUT    /api/cosechas/{id}         → Editar cosecha (recibe CosechaDTO)
 *   PATCH  /api/cosechas/{id}/eliminar  → Eliminación lógica
 *   PATCH  /api/cosechas/{id}/restaurar → Restaurar cosecha
 *
 * NOTA: tipoCultivo NO existe en ningún endpoint. Se obtiene desde Cultivo.
 */
@RestController
@RequestMapping("/api/cosechas")
@CrossOrigin("*")
@Tag(name = "Cosecha-Controller",
     description = "HU3: Registro y gestión de cosechas. La relación con el cultivo se realiza " +
                   "únicamente mediante idCultivo. No existe tipoCultivo en este módulo.")
public class CosechaController {

    @Autowired
    private CosechaService service;

    // ─────────────────────────── CONSULTAS ───────────────────────────

    @GetMapping
    @Operation(
        summary = "Listar todas las cosechas",
        description = "Retorna la lista completa de cosechas con sus relaciones " +
                      "(campo, cultivo, usuario) resueltas."
    )
    @ApiResponse(responseCode = "200", description = "Lista de cosechas obtenida correctamente")
    public ResponseEntity<List<Cosecha>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar cosecha por ID",
        description = "Retorna la cosecha con el ID especificado incluyendo campo, cultivo y usuario."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cosecha encontrada"),
        @ApiResponse(responseCode = "404", description = "Cosecha no encontrada", content = @Content)
    })
    public ResponseEntity<Cosecha> listarPorId(
            @Parameter(description = "ID de la cosecha", example = "1")
            @PathVariable Long id) {
        return service.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(
        summary = "Listar cosechas por estado",
        description = "Filtra cosechas por estado activo (true) o inactivo (false)."
    )
    @ApiResponse(responseCode = "200", description = "Lista filtrada por estado")
    public ResponseEntity<List<Cosecha>> listarPorEstado(
            @Parameter(description = "Estado: true=activo, false=inactivo", example = "true")
            @PathVariable Boolean estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    @GetMapping("/resumen")
    @Operation(
        summary = "Resumen de cosechas por cultivo",
        description = "Retorna el total cosechado (kg y unidades) agrupado por nombre de cultivo. " +
                      "Solo incluye cosechas con estado activo."
    )
    @ApiResponse(responseCode = "200", description = "Resumen generado correctamente")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumen() {
        return ResponseEntity.ok(service.obtenerResumen());
    }

    // ─────────────────────────── ESCRITURA ───────────────────────────

    @PostMapping
    @Operation(
        summary = "Registrar cosecha",
        description = "Registra una nueva cosecha. Se requieren idCampo, idCultivo e idUsuario. " +
                      "El tipo de cultivo se obtiene automáticamente desde el Cultivo relacionado. " +
                      "cantidadKg se almacena exactamente como se recibe (sin conversión)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cosecha registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ID no encontrado", content = @Content)
    })
    public ResponseEntity<Cosecha> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "DTO con los datos de la cosecha. No incluir tipoCultivo.",
                required = true,
                content = @Content(schema = @Schema(implementation = CosechaDTO.class))
            )
            @Valid @RequestBody CosechaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Editar cosecha",
        description = "Actualiza los datos de una cosecha existente. Se pueden enviar idCampo, " +
                      "idCultivo e idUsuario para actualizar las relaciones. cantidadKg sin conversión."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cosecha actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cosecha no encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "ID de relación no encontrado", content = @Content)
    })
    public ResponseEntity<Cosecha> editar(
            @Parameter(description = "ID de la cosecha a editar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CosechaDTO dto) {
        return ResponseEntity.ok(service.editar(id, dto));
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(
        summary = "Eliminación lógica de cosecha",
        description = "Cambia el estado de la cosecha a inactivo (estado=false) y registra deletedAt."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cosecha eliminada lógicamente"),
        @ApiResponse(responseCode = "404", description = "Cosecha no encontrada", content = @Content)
    })
    public ResponseEntity<Cosecha> eliminar(
            @Parameter(description = "ID de la cosecha a eliminar", example = "1")
            @PathVariable Long id) {
        Cosecha eliminada = service.eliminar(id);
        return (eliminada != null) ? ResponseEntity.ok(eliminada) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(
        summary = "Restaurar cosecha",
        description = "Reactiva una cosecha eliminada lógicamente (estado=true) y registra restoredAt."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cosecha restaurada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cosecha no encontrada", content = @Content)
    })
    public ResponseEntity<Cosecha> restaurar(
            @Parameter(description = "ID de la cosecha a restaurar", example = "1")
            @PathVariable Long id) {
        Cosecha restaurada = service.restaurar(id);
        return (restaurada != null) ? ResponseEntity.ok(restaurada) : ResponseEntity.notFound().build();
    }
}
