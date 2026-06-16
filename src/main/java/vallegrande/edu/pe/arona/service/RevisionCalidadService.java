package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.RevisionCalidad;
import vallegrande.edu.pe.arona.model.RevisionCalidadDTO;

import java.util.List;
import java.util.Optional;

public interface RevisionCalidadService {
    List<RevisionCalidad> listarTodos();
    Optional<RevisionCalidad> listarPorId(Long id);
    List<RevisionCalidad> listarPorCosecha(Long idCosecha);
    RevisionCalidad crear(RevisionCalidadDTO dto);
    RevisionCalidad editar(Long id, RevisionCalidadDTO dto);
    RevisionCalidad eliminar(Long id);
    RevisionCalidad restaurar(Long id);
}
