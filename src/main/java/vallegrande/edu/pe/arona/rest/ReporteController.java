package vallegrande.edu.pe.arona.rest;

import vallegrande.edu.pe.arona.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin("*")
@Tag(name = "Reporte-Controller", description = "HU9: Reportes de producción")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @GetMapping("/produccion")
    @Operation(summary = "Reporte de producción", description = "Datos de cosecha agrupados por tipo de cultivo y campo")
    public ResponseEntity<Map<String, Object>> reporteProduccion(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {
        return ResponseEntity.ok(service.obtenerReporteProduccion(fechaInicio, fechaFin));
    }
}
