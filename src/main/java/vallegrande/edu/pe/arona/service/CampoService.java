package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.Campo;

import java.util.List;
import java.util.Optional;

public interface CampoService {
    List<Campo> listarTodos();
    Optional<Campo> listarPorId(Long id);
    List<Campo> listarPorEstado(Boolean estado);
    List<Campo> buscarPorNombre(String nombre);
    Campo crear(Campo campo);
    Campo editar(Long id, Campo campo);
    Campo eliminar(Long id);
    Campo restaurar(Long id);
}

