package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.Cultivo;
import vallegrande.edu.pe.arona.model.CultivoDTO;
import java.util.List;
import java.util.Optional;

public interface CultivoService {
    List<Cultivo> listarTodos();
    Optional<Cultivo> listarPorId(Long id);
    List<Cultivo> listarPorEstado(Boolean estado);
    List<Cultivo> listarPorTipoCultivo(String tipoCultivo);
    List<Cultivo> listarPorEstadoSalud(String estadoSalud);
    List<Cultivo> buscarPorNombre(String nombre);
    Cultivo crear(CultivoDTO dto);
    Cultivo editar(Long id, CultivoDTO dto);
    Cultivo eliminar(Long id);
    Cultivo restaurar(Long id);
}
