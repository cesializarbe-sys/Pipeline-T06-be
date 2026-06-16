package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.Campo;
import vallegrande.edu.pe.arona.service.CampoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/campos")
@CrossOrigin("*")
@Tag(name = "Campo-Controller", description = "Gestión de campos / terrenos de cultivo")
public class CampoController {

    @Autowired
    private CampoService campoService;

    @GetMapping
    @Operation(summary = "Listar campos", description = "Obtiene la lista de todos los campos")
    public ResponseEntity<List<Campo>> listarTodos() {
        return ResponseEntity.ok(campoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene un campo por su ID")
    public ResponseEntity<Campo> listarPorId(@PathVariable Long id) {
        return campoService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado", description = "Filtra campos activos o inactivos")
    public ResponseEntity<List<Campo>> listarPorEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(campoService.listarPorEstado(estado));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nombre", description = "Búsqueda parcial por nombre (LIKE %nombre%) ignorando mayúsculas/minúsculas")
    public ResponseEntity<List<Campo>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(campoService.buscarPorNombre(nombre));
    }

    @PostMapping
    @Operation(summary = "Crear campo", description = "Registra un nuevo campo. Valida nombre único y hectáreas > 0")
    public ResponseEntity<Campo> crear(@Valid @RequestBody Campo campo) {
        Campo nuevo = campoService.crear(campo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar campo", description = "Modifica los datos de un campo existente")
    public ResponseEntity<Campo> editar(@PathVariable Long id, @Valid @RequestBody Campo campo) {
        Campo editado = campoService.editar(id, campo);
        return ResponseEntity.ok(editado);
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (lógico)", description = "Desactiva el campo y registra fecha de eliminación")
    public ResponseEntity<Campo> eliminar(@PathVariable Long id) {
        Campo eliminado = campoService.eliminar(id);
        return ResponseEntity.ok(eliminado);
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar", description = "Reactiva el campo y registra fecha de restauración")
    public ResponseEntity<Campo> restaurar(@PathVariable Long id) {
        Campo restaurado = campoService.restaurar(id);
        return ResponseEntity.ok(restaurado);
    }
}

