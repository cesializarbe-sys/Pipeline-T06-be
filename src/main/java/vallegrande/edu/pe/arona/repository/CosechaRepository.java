package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.Cosecha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio de Cosechas.
 * NOTA: findByTipoCultivo fue eliminado. La búsqueda por tipo de cultivo
 * debe realizarse navegando la relación @ManyToOne con Cultivo.
 */
@Repository
public interface CosechaRepository extends JpaRepository<Cosecha, Long> {

    /**
     * Filtra cosechas por estado (true=activo, false=inactivo).
     */
    List<Cosecha> findByEstado(Boolean estado);

    /**
     * Filtra cosechas por ID de campo usando navegación @ManyToOne.
     */
    List<Cosecha> findByCampo_IdCampo(Long idCampo);

    /**
     * Filtra cosechas por ID de cultivo usando navegación @ManyToOne.
     */
    List<Cosecha> findByCultivo_IdCultivo(Long idCultivo);

    /**
     * Filtra cosechas por ID de usuario usando navegación @ManyToOne.
     */
    List<Cosecha> findByUsuario_IdUsuario(Integer idUsuario);

    /**
     * Búsqueda de cosechas en un rango de fechas con estado activo.
     */
    @Query("SELECT c FROM Cosecha c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin AND c.estado = true")
    List<Cosecha> findByFechaBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);
}
