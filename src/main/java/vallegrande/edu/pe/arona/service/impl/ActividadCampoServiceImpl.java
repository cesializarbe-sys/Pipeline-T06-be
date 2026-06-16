package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.ActividadCampo;
import vallegrande.edu.pe.arona.repository.ActividadCampoRepository;
import vallegrande.edu.pe.arona.service.ActividadCampoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadCampoServiceImpl implements ActividadCampoService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private ActividadCampoRepository repository;

    @Override
    public List<ActividadCampo> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<ActividadCampo> listarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<ActividadCampo> listarPorCampo(Long idCampo) {
        return repository.findByIdCampo(idCampo);
    }

    @Override
    public List<ActividadCampo> listarPorUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario);
    }

    @Override
    public ActividadCampo crear(ActividadCampo actividad) {
        actividad.setEstado(true);
        actividad.setCreatedAt(LocalDateTime.now(ZONA_LIMA));
        return repository.save(actividad);
    }

    @Override
    public ActividadCampo editar(Long id, ActividadCampo datos) {
        return repository.findById(id).map(a -> {
            a.setTipoActividad(datos.getTipoActividad());
            a.setFecha(datos.getFecha());
            a.setIdCampo(datos.getIdCampo());
            a.setIdCultivo(datos.getIdCultivo());
            a.setObservaciones(datos.getObservaciones());
            a.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }

    @Override
    public ActividadCampo eliminar(Long id) {
        return repository.findById(id).map(a -> {
            a.setEstado(false);
            a.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElse(null);
    }

    @Override
    public ActividadCampo restaurar(Long id) {
        return repository.findById(id).map(a -> {
            a.setEstado(true);
            a.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElse(null);
    }
}

