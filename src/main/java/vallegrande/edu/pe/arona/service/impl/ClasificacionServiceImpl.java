package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.Clasificacion;
import vallegrande.edu.pe.arona.repository.ClasificacionRepository;
import vallegrande.edu.pe.arona.service.ClasificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClasificacionServiceImpl implements ClasificacionService {

    @Autowired
    private ClasificacionRepository repository;

    @Override
    public List<Clasificacion> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Clasificacion> listarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Clasificacion> listarPorCosecha(Long idCosecha) {
        return repository.findByIdCosecha(idCosecha);
    }

    @Override
    public List<Map<String, Object>> obtenerResumenPorCosecha(Long idCosecha) {
        List<Clasificacion> clasificaciones = repository.findByIdCosecha(idCosecha);
        Map<String, List<Clasificacion>> porCalibre = clasificaciones.stream()
                .collect(Collectors.groupingBy(Clasificacion::getCalibre));

        List<Map<String, Object>> resumen = new ArrayList<>();
        porCalibre.forEach((calibre, lista) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("calibre", calibre);
            item.put("totalKg", lista.stream()
                    .map(Clasificacion::getCantidadKg)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            item.put("totalUnidades", lista.stream()
                    .map(Clasificacion::getCantidadUnidades)
                    .filter(Objects::nonNull)
                    .reduce(0, Integer::sum));
            item.put("aptosExportacion", lista.stream()
                    .filter(c -> Boolean.TRUE.equals(c.getAptoExportacion()))
                    .count());
            item.put("noAptos", lista.stream()
                    .filter(c -> !Boolean.TRUE.equals(c.getAptoExportacion()))
                    .count());
            resumen.add(item);
        });
        return resumen;
    }

    @Override
    public List<Clasificacion> listarAptosExportacion() {
        return repository.findByAptoExportacionTrueAndEstadoTrue();
    }

    @Override
    public Clasificacion crear(Clasificacion clasificacion) {
        // Si la fruta está dañada, no es apta para exportación
        if ("DAÑADA".equals(clasificacion.getEstadoFruta())) {
            clasificacion.setAptoExportacion(false);
        }
        clasificacion.setEstado(true);
        clasificacion.setCreatedAt(LocalDateTime.now());
        return repository.save(clasificacion);
    }

    @Override
    public Clasificacion editar(Long id, Clasificacion datos) {
        return repository.findById(id).map(c -> {
            c.setCalibre(datos.getCalibre());
            c.setEstadoFruta(datos.getEstadoFruta());
            c.setCantidadKg(datos.getCantidadKg());
            c.setCantidadUnidades(datos.getCantidadUnidades());
            c.setAptoExportacion(datos.getAptoExportacion());
            c.setFecha(datos.getFecha());
            c.setUpdatedAt(LocalDateTime.now());
            return repository.save(c);
        }).orElseThrow(() -> new RuntimeException("Clasificación no encontrada"));
    }

    @Override
    public Clasificacion eliminar(Long id) {
        return repository.findById(id).map(c -> {
            c.setEstado(false);
            c.setDeletedAt(LocalDateTime.now());
            return repository.save(c);
        }).orElse(null);
    }

    @Override
    public Clasificacion restaurar(Long id) {
        return repository.findById(id).map(c -> {
            c.setEstado(true);
            c.setRestoredAt(LocalDateTime.now());
            return repository.save(c);
        }).orElse(null);
    }
}

