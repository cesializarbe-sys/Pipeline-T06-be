package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.Campo;
import vallegrande.edu.pe.arona.repository.CampoRepository;
import vallegrande.edu.pe.arona.service.CampoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class CampoServiceImpl implements CampoService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private CampoRepository campoRepository;

    @Override
    public List<Campo> listarTodos() {
        return campoRepository.findAll();
    }

    @Override
    public Optional<Campo> listarPorId(Long id) {
        return campoRepository.findById(id);
    }

    @Override
    public List<Campo> listarPorEstado(Boolean estado) {
        return campoRepository.findByEstado(estado);
    }

    @Override
    public List<Campo> buscarPorNombre(String nombre) {
        return campoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Campo crear(Campo campo) {
        if (campoRepository.existsByNombreIgnoreCase(campo.getNombre())) {
            throw new RuntimeException("Ya existe un campo con el nombre: " + campo.getNombre());
        }
        LocalDateTime now = LocalDateTime.now(ZONA_LIMA);
        campo.setEstado(true);
        campo.setCreatedAt(now);
        campo.setUpdatedAt(null);
        campo.setDeletedAt(null);
        campo.setRestoredAt(null);
        return campoRepository.save(campo);
    }

    @Override
    public Campo editar(Long id, Campo datos) {
        return campoRepository.findById(id).map(campo -> {
            // Validar unicidad de nombre (si cambió)
            if (!campo.getNombre().equalsIgnoreCase(datos.getNombre())
                    && campoRepository.existsByNombreIgnoreCase(datos.getNombre())) {
                throw new RuntimeException("Ya existe un campo con el nombre: " + datos.getNombre());
            }
            campo.setNombre(datos.getNombre());
            campo.setUbicacion(datos.getUbicacion());
            campo.setHectareas(datos.getHectareas());
            campo.setResponsable(datos.getResponsable());
            campo.setSistemaRiego(datos.getSistemaRiego());
            campo.setTipoSuelo(datos.getTipoSuelo());
            campo.setZona(datos.getZona());
            campo.setObservaciones(datos.getObservaciones());
            campo.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return campoRepository.save(campo);
        }).orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + id));
    }

    @Override
    public Campo eliminar(Long id) {
        return campoRepository.findById(id).map(campo -> {
            campo.setEstado(false);
            campo.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return campoRepository.save(campo);
        }).orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + id));
    }

    @Override
    public Campo restaurar(Long id) {
        return campoRepository.findById(id).map(campo -> {
            campo.setEstado(true);
            campo.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return campoRepository.save(campo);
        }).orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + id));
    }
}
