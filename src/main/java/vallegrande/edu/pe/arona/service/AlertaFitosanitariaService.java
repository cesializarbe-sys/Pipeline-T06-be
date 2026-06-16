package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.AlertaFitosanitaria;
import vallegrande.edu.pe.arona.dto.AlertaFitosanitariaDTO;

import java.util.List;
import java.util.Optional;

public interface AlertaFitosanitariaService {
    List<AlertaFitosanitaria> listarTodos();
    Optional<AlertaFitosanitaria> listarPorId(Long id);
    List<AlertaFitosanitaria> listarPendientes();
    List<AlertaFitosanitaria> listarPorCampo(Long idCampo);
    AlertaFitosanitaria crear(AlertaFitosanitariaDTO dto);
    AlertaFitosanitaria editar(Long id, AlertaFitosanitariaDTO dto);
    AlertaFitosanitaria atender(Long id, String solucionAplicada);
    AlertaFitosanitaria eliminar(Long id);
    AlertaFitosanitaria restaurar(Long id);
}
