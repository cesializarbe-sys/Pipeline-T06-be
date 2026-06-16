package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.AlertaFitosanitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaFitosanitariaRepository extends JpaRepository<AlertaFitosanitaria, Long> {
    List<AlertaFitosanitaria> findByEstado(Boolean estado);
    List<AlertaFitosanitaria> findByEstadoAlerta(String estadoAlerta);

    /**
     * Busca alertas por ID de campo usando navegación @ManyToOne.
     */
    List<AlertaFitosanitaria> findByCampo_IdCampo(Long idCampo);

    /**
     * Alias para compatibilidad con AlertaFitosanitariaServiceImpl.
     */
    default List<AlertaFitosanitaria> findByIdCampo(Long idCampo) {
        return findByCampo_IdCampo(idCampo);
    }

    /**
     * Busca alertas por ID de cultivo usando navegación @ManyToOne.
     */
    List<AlertaFitosanitaria> findByCultivo_IdCultivo(Long idCultivo);
}
