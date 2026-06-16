package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.Clasificacion;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClasificacionService {
    List<Clasificacion> listarTodos();
    Optional<Clasificacion> listarPorId(Long id);
    List<Clasificacion> listarPorCosecha(Long idCosecha);
    List<Map<String, Object>> obtenerResumenPorCosecha(Long idCosecha);
    List<Clasificacion> listarAptosExportacion();
    Clasificacion crear(Clasificacion clasificacion);
    Clasificacion editar(Long id, Clasificacion clasificacion);
    Clasificacion eliminar(Long id);
    Clasificacion restaurar(Long id);
}

