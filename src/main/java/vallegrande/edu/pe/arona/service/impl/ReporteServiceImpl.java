package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.Cosecha;
import vallegrande.edu.pe.arona.repository.CosechaRepository;
import vallegrande.edu.pe.arona.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de reportes de producción.
 * HU9: Provee datos agregados (JSON) para que el frontend genere gráficos.
 *
 * CORRECCIÓN: El agrupamiento ya no usa Cosecha.tipoCultivo (campo eliminado).
 * Ahora se agrupa por cultivo.getTipoCultivo() navegando la relación @ManyToOne.
 */
@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private CosechaRepository cosechaRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerReporteProduccion(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cosecha> cosechas = cosechaRepository.findByFechaBetween(fechaInicio, fechaFin);

        Map<String, Object> reporte = new LinkedHashMap<>();
        reporte.put("periodo", Map.of("inicio", fechaInicio.toString(), "fin", fechaFin.toString()));
        reporte.put("totalCosechas", cosechas.size());
        reporte.put("totalKg", cosechas.stream()
                .map(Cosecha::getCantidadKg)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Desglose por tipo de cultivo obtenido desde la relación Cultivo (no desde tipoCultivo)
        Map<String, List<Cosecha>> porTipo = cosechas.stream()
                .filter(c -> c.getCultivo() != null && c.getCultivo().getTipoCultivo() != null)
                .collect(Collectors.groupingBy(c -> c.getCultivo().getTipoCultivo()));

        List<Map<String, Object>> desglose = new ArrayList<>();
        porTipo.forEach((tipo, lista) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("tipoCultivo", tipo);  // valor obtenido desde Cultivo, no de Cosecha
            item.put("cantidadCosechas", lista.size());
            item.put("totalKg", lista.stream()
                    .map(Cosecha::getCantidadKg)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            // Desglose por campo
            item.put("campos", lista.stream()
                    .filter(c -> c.getCampo() != null)
                    .collect(Collectors.groupingBy(
                            c -> c.getCampo().getNombre(),
                            Collectors.counting()
                    )));
            desglose.add(item);
        });
        reporte.put("desglosePorTipo", desglose);

        return reporte;
    }
}
