package vallegrande.edu.pe.arona.repository;

import vallegrande.edu.pe.arona.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(Boolean estado);
    List<Producto> findByTipoCultivo(String tipoCultivo);
    List<Producto> findByCantidadKgLessThanAndEstadoTrue(BigDecimal umbral);
    List<Producto> findByEstadoTrueOrderByFechaIngresoAsc();
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}

