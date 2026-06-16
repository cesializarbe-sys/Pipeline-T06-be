package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.Campo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampoRepository extends JpaRepository<Campo, Long> {
    List<Campo> findByEstado(Boolean estado);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Campo> findByNombreContainingIgnoreCase(String nombre);
}

