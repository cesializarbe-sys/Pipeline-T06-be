package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.model.Producto;
import vallegrande.edu.pe.arona.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
@Tag(name = "Producto-Controller", description = "HU8: Seguimiento del inventario de fruta")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todo el inventario")
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<Producto> listarPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.listarPorId(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado")
    public ResponseEntity<List<Producto>> listarPorEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(productoService.listarPorEstado(estado));
    }

    @GetMapping("/tipo/{tipoCultivo}")
    @Operation(summary = "Filtrar por tipo de fruta", description = "MANDARINA, PALTA, ARANDANO, CAQUI")
    public ResponseEntity<List<Producto>> listarPorTipo(@PathVariable String tipoCultivo) {
        return ResponseEntity.ok(productoService.listarPorTipoCultivo(tipoCultivo));
    }

    @GetMapping("/alertas-stock")
    @Operation(summary = "Productos con stock bajo", description = "Productos cuyo stock está bajo el umbral mínimo")
    public ResponseEntity<List<Producto>> listarAlertasStock() {
        return ResponseEntity.ok(productoService.listarAlertasStock());
    }

    @GetMapping("/antiguedad")
    @Operation(summary = "Por antigüedad", description = "Ordenado por fecha de ingreso (más antiguo primero)")
    public ResponseEntity<List<Producto>> listarPorAntiguedad() {
        return ResponseEntity.ok(productoService.listarOrdenadoPorAntiguedad());
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nombre", description = "Búsqueda parcial por nombre (LIKE %nombre%) ignorando mayúsculas/minúsculas")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @PostMapping
    @Operation(summary = "Ingresar producto al inventario")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar producto")
    public ResponseEntity<Producto> editar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto editado = productoService.editar(id, producto);
        if (editado != null) {
            return ResponseEntity.ok(editado);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar (lógico)")
    public ResponseEntity<Producto> eliminar(@PathVariable Long id) {
        Producto eliminado = productoService.eliminar(id);
        if (eliminado != null) {
            return ResponseEntity.ok(eliminado);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar")
    public ResponseEntity<Producto> restaurar(@PathVariable Long id) {
        Producto restaurado = productoService.restaurar(id);
        if (restaurado != null) {
            return ResponseEntity.ok(restaurado);
        }
        return ResponseEntity.notFound().build();
    }
}

