package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.ActividadCampo;

import java.util.List;
import java.util.Optional;

public interface ActividadCampoService {
    List<ActividadCampo> listarTodos();
    Optional<ActividadCampo> listarPorId(Long id);
    List<ActividadCampo> listarPorCampo(Long idCampo);
    List<ActividadCampo> listarPorUsuario(Integer idUsuario);
    ActividadCampo crear(ActividadCampo actividad);
    ActividadCampo editar(Long id, ActividadCampo actividad);
    ActividadCampo eliminar(Long id);
    ActividadCampo restaurar(Long id);
}

