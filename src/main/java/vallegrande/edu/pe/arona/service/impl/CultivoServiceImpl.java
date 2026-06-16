package vallegrande.edu.pe.arona.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vallegrande.edu.pe.arona.model.Campo;
import vallegrande.edu.pe.arona.model.Cultivo;
import vallegrande.edu.pe.arona.model.CultivoDTO;
import vallegrande.edu.pe.arona.repository.CampoRepository;
import vallegrande.edu.pe.arona.repository.CultivoRepository;
import vallegrande.edu.pe.arona.service.CultivoService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class CultivoServiceImpl implements CultivoService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private CultivoRepository repository;

    @Autowired
    private CampoRepository campoRepository;

    @Override
    public List<Cultivo> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Cultivo> listarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Cultivo> listarPorEstado(Boolean estado) {
        return repository.findByEstado(estado);
    }

    @Override
    public List<Cultivo> listarPorTipoCultivo(String tipoCultivo) {
        return repository.findByTipoCultivo(tipoCultivo);
    }

    @Override
    public List<Cultivo> listarPorEstadoSalud(String estadoSalud) {
        return repository.findByEstadoSalud(estadoSalud);
    }

    @Override
    public List<Cultivo> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Cultivo crear(CultivoDTO dto) {
        Cultivo cultivo = new Cultivo();

        // Resolver campo por ID
        if (dto.getIdCampo() != null) {
            Campo campo = campoRepository.findById(dto.getIdCampo())
                    .orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + dto.getIdCampo()));
            cultivo.setCampo(campo);
        }

        cultivo.setNombre(dto.getNombre());
        cultivo.setTipoCultivo(dto.getTipoCultivo());
        if (
            dto.getFrecuenciaRiegoDias() != null &&
            dto.getFrecuenciaRiegoDias() <= 0
        ) {
            throw new RuntimeException(
        "La frecuencia de riego debe ser mayor a 0"
            );
        }
        cultivo.setFrecuenciaRiegoDias(dto.getFrecuenciaRiegoDias());
        if (
            dto.getTemperaturaIdeal() != null &&
            (
                dto.getTemperaturaIdeal() < 5 ||
                dto.getTemperaturaIdeal() > 45
            )
        ) {
            throw new RuntimeException(
        "La temperatura debe estar entre 5 y 45 grados"
            );
        }
        cultivo.setTemperaturaIdeal(dto.getTemperaturaIdeal());
        cultivo.setFechaSiembra(dto.getFechaSiembra());
        cultivo.setRequiereSombra(dto.getRequiereSombra());
        cultivo.setEstadoSalud(dto.getEstadoSalud() != null ? dto.getEstadoSalud() : "BUENO");
        cultivo.setObservaciones(dto.getObservaciones());
        cultivo.setEstado(true);
        cultivo.setCreatedAt(LocalDateTime.now(ZONA_LIMA));
        return repository.save(cultivo);
    }

    @Override
    public Cultivo editar(Long id, CultivoDTO dto) {
        return repository.findById(id).map(c -> {
            // Resolver campo por ID
            if (dto.getIdCampo() != null) {
                Campo campo = campoRepository.findById(dto.getIdCampo())
                        .orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + dto.getIdCampo()));
                c.setCampo(campo);
            } else {
                c.setCampo(null);
            }

            c.setNombre(dto.getNombre());
            c.setTipoCultivo(dto.getTipoCultivo());
            if (
                dto.getFrecuenciaRiegoDias() != null &&
                dto.getFrecuenciaRiegoDias() <= 0
            ) {
                throw new RuntimeException(
            "La frecuencia de riego debe ser mayor a 0"
                );
            }
            c.setFrecuenciaRiegoDias(dto.getFrecuenciaRiegoDias());
            if (
                dto.getTemperaturaIdeal() != null &&
                (
                    dto.getTemperaturaIdeal() < 5 ||
                    dto.getTemperaturaIdeal() > 45
                )
            ) {
                throw new RuntimeException(
            "La temperatura debe estar entre 5 y 45 grados"
                );
            }
            c.setTemperaturaIdeal(dto.getTemperaturaIdeal());
            c.setFechaSiembra(dto.getFechaSiembra());
            c.setRequiereSombra(dto.getRequiereSombra());
            c.setEstadoSalud(dto.getEstadoSalud());
            c.setObservaciones(dto.getObservaciones());
            c.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cultivo no encontrado con ID: " + id));
    }

    @Override
    public Cultivo eliminar(Long id) {
        return repository.findById(id).map(c -> {
            c.setEstado(false);
            c.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElse(null);
    }

    @Override
    public Cultivo restaurar(Long id) {
        return repository.findById(id).map(c -> {
            c.setEstado(true);
            c.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElse(null);
    }
}
