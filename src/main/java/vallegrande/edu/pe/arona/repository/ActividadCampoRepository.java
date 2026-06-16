package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.ActividadCampo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadCampoRepository extends JpaRepository<ActividadCampo, Long> {
    List<ActividadCampo> findByEstado(Boolean estado);
    List<ActividadCampo> findByIdCampo(Long idCampo);
    List<ActividadCampo> findByIdUsuario(Integer idUsuario);
    List<ActividadCampo> findByIdCultivo(Long idCultivo);
}

