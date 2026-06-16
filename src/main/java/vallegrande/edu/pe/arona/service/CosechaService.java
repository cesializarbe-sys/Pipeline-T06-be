package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.dto.CosechaDTO;
import vallegrande.edu.pe.arona.model.Cosecha;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz del servicio de Cosechas.
 * NOTA: tipoCultivo NO está presente en ninguna firma de método.
 * La relación con Cultivo se maneja únicamente mediante idCultivo en el DTO.
 */
public interface CosechaService {
    List<Cosecha> listarTodos();
    Optional<Cosecha> listarPorId(Long id);
    List<Cosecha> listarPorEstado(Boolean estado);
    List<Map<String, Object>> obtenerResumen();
    Cosecha crear(CosechaDTO dto);
    Cosecha editar(Long id, CosechaDTO dto);
    Cosecha eliminar(Long id);
    Cosecha restaurar(Long id);
}
