package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Integer id);
    List<Usuario> listarPorEstado(Boolean estado);
    List<Usuario> listarPorArea(String area);
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Integer id, Usuario usuario);
    Usuario eliminarLogico(Integer id);
    Usuario restaurarLogico(Integer id);
    Optional<Usuario> login(String correo, String password);
}

