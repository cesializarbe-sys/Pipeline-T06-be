package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.Producto;
import vallegrande.edu.pe.arona.repository.ProductoRepository;
import vallegrande.edu.pe.arona.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> listarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> listarPorEstado(Boolean estado) {
        return productoRepository.findByEstado(estado);
    }

    @Override
    public List<Producto> listarPorTipoCultivo(String tipoCultivo) {
        return productoRepository.findByTipoCultivo(tipoCultivo);
    }

    @Override
    public List<Producto> listarAlertasStock() {
        // Retorna productos cuyo stock está por debajo de su umbral mínimo
        List<Producto> todos = productoRepository.findByEstado(true);
        return todos.stream()
                .filter(p -> p.getCantidadKg().compareTo(p.getUmbralMinimo()) < 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarOrdenadoPorAntiguedad() {
        return productoRepository.findByEstadoTrueOrderByFechaIngresoAsc();
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Producto crear(Producto producto) {
        LocalDateTime now = LocalDateTime.now(ZONA_LIMA);
        producto.setEstado(true);
        producto.setCreatedAt(now);
        producto.setUpdatedAt(null);
        producto.setDeletedAt(null);
        producto.setRestoredAt(null);
        if (producto.getUmbralMinimo() == null) {
            producto.setUmbralMinimo(new BigDecimal("100.00"));
        }
        return productoRepository.save(producto);
    }

    @Override
    public Producto editar(Long id, Producto producto) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isPresent()) {
            Producto p = existente.get();
            p.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            p.setNombre(producto.getNombre());
            p.setTipoCultivo(producto.getTipoCultivo());
            p.setDescripcion(producto.getDescripcion());
            p.setCantidadKg(producto.getCantidadKg());
            p.setCantidadCajas(producto.getCantidadCajas());
            p.setUnidadMedida(producto.getUnidadMedida());
            p.setFechaIngreso(producto.getFechaIngreso());
            p.setUmbralMinimo(producto.getUmbralMinimo());
            p.setIdCosecha(producto.getIdCosecha());
            return productoRepository.save(p);
        }
        return null;
    }

    @Override
    public Producto eliminar(Long id) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isPresent()) {
            Producto producto = existente.get();
            producto.setEstado(false);
            producto.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return productoRepository.save(producto);
        }
        return null;
    }

    @Override
    public Producto restaurar(Long id) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isPresent()) {
            Producto producto = existente.get();
            producto.setEstado(true);
            producto.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return productoRepository.save(producto);
        }
        return null;
    }
}

