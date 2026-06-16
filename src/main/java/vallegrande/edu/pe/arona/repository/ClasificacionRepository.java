package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.Clasificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasificacionRepository extends JpaRepository<Clasificacion, Long> {
    List<Clasificacion> findByEstado(Boolean estado);
    List<Clasificacion> findByIdCosecha(Long idCosecha);
    List<Clasificacion> findByAptoExportacionTrueAndEstadoTrue();
}

