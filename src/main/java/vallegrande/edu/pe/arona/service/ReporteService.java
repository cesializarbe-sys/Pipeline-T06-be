package vallegrande.edu.pe.arona.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * Servicio de reportes de producción.
 * HU9: Provee datos agregados para que el frontend genere gráficos.
 * Nota: El reporte de exportación fue eliminado junto con el módulo de Envíos.
 */
public interface ReporteService {
    Map<String, Object> obtenerReporteProduccion(LocalDate fechaInicio, LocalDate fechaFin);
}
