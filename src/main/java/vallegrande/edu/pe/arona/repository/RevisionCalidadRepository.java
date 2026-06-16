package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.RevisionCalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevisionCalidadRepository extends JpaRepository<RevisionCalidad, Long> {
    List<RevisionCalidad> findByEstado(Boolean estado);

    /**
     * Busca revisiones por ID de cosecha usando la relación @ManyToOne.
     * Spring Data JPA permite navegar relaciones con "_" en el nombre del método.
     */
    List<RevisionCalidad> findByCosecha_IdCosecha(Long idCosecha);

    /**
     * Alias para compatibilidad con RevisionCalidadServiceImpl.
     * Llama a findByCosecha_IdCosecha internamente.
     */
    default List<RevisionCalidad> findByIdCosecha(Long idCosecha) {
        return findByCosecha_IdCosecha(idCosecha);
    }

    List<RevisionCalidad> findByCumpleRequisitos(Boolean cumple);
    boolean existsByCosecha_IdCosecha(Long idCosecha);

    @Query("SELECT r FROM RevisionCalidad r WHERE r.notificado = false AND r.cumpleRequisitos = false AND r.estado = true")
    List<RevisionCalidad> findNoNotificadas();
}
