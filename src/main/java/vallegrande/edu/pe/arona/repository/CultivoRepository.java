package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CultivoRepository extends JpaRepository<Cultivo, Long> {
    List<Cultivo> findByEstado(Boolean estado);
    List<Cultivo> findByTipoCultivo(String tipoCultivo);
    List<Cultivo> findByEstadoSalud(String estadoSalud);
    List<Cultivo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca cultivos por ID de campo usando navegación @ManyToOne.
     */
    List<Cultivo> findByCampo_IdCampo(Long idCampo);
}
