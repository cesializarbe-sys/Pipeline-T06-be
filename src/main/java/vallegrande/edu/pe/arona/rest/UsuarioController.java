package vallegrande.edu.pe.arona.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vallegrande.edu.pe.arona.model.Usuario;
import vallegrande.edu.pe.arona.model.LoginRequest;
import vallegrande.edu.pe.arona.model.ErrorResponse;
import vallegrande.edu.pe.arona.service.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
@Tag(name = "Usuario-Controller", description = "Gestión de personal de Arona")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado", description = "Filtra usuarios activos o inactivos")
    public ResponseEntity<List<Usuario>> listarPorEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    @GetMapping("/area/{area}")
    @Operation(summary = "Listar por área", description = "CAMPO, PLANTA, ALMACEN, ADMINISTRACION, CALIDAD")
    public ResponseEntity<List<Usuario>> listarPorArea(@PathVariable String area) {
        return ResponseEntity.ok(service.listarPorArea(area));
    }

    @PostMapping
    @Operation(summary = "Registrar usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario nuevo = service.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<Usuario> editar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.actualizar(id, usuario));
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (Lógico)")
    public ResponseEntity<Usuario> eliminar(@PathVariable Integer id) {
        Usuario eliminado = service.eliminarLogico(id);
        if (eliminado != null) {
            return ResponseEntity.ok(eliminado);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar")
    public ResponseEntity<Usuario> restaurar(@PathVariable Integer id) {
        Usuario restaurado = service.restaurarLogico(id);
        if (restaurado != null) {
            return ResponseEntity.ok(restaurado);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticación de usuario", description = "Valida credenciales y retorna los datos del usuario")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var usuarioOpt = service.login(loginRequest.getCorreo(), loginRequest.getPassword());
        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(usuarioOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales inválidas"));
        }
    }
}

