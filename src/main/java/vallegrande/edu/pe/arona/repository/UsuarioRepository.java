package vallegrande.edu.pe.arona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vallegrande.edu.pe.arona.model.Usuario;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByEstado(Boolean estado);
    List<Usuario> findByArea(String area);
    List<Usuario> findByRol(String rol);
    Optional<Usuario> findByCorreo(String correo);
}

