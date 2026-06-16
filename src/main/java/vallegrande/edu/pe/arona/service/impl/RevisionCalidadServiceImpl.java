package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.Cosecha;
import vallegrande.edu.pe.arona.model.RevisionCalidad;
import vallegrande.edu.pe.arona.model.Usuario;
import vallegrande.edu.pe.arona.model.RevisionCalidadDTO;
import vallegrande.edu.pe.arona.repository.CosechaRepository;
import vallegrande.edu.pe.arona.repository.RevisionCalidadRepository;
import vallegrande.edu.pe.arona.repository.UsuarioRepository;
import vallegrande.edu.pe.arona.service.RevisionCalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class RevisionCalidadServiceImpl implements RevisionCalidadService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private RevisionCalidadRepository repository;

    @Autowired
    private CosechaRepository cosechaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<RevisionCalidad> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<RevisionCalidad> listarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<RevisionCalidad> listarPorCosecha(Long idCosecha) {
        return repository.findByIdCosecha(idCosecha);
    }

    @Override
    public RevisionCalidad crear(RevisionCalidadDTO dto) {
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new RuntimeException(
        "La fecha de revisión no puede ser futura"
            );
        }

        if (repository.existsByCosecha_IdCosecha(dto.getIdCosecha())) {
            throw new RuntimeException(
        "Esta cosecha ya tiene una revisión registrada"
            );
        }

        if (
            !Boolean.TRUE.equals(dto.getCumpleRequisitos()) &&
            (
                dto.getObservaciones() == null ||
                dto.getObservaciones().trim().isEmpty()
            )
        ) {
            throw new RuntimeException(
        "Las observaciones son obligatorias cuando la revisión es rechazada"
            );
        }
        RevisionCalidad revision = new RevisionCalidad();

        // Resolver cosecha por ID
        Cosecha cosecha = cosechaRepository.findById(dto.getIdCosecha())
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada con ID: " + dto.getIdCosecha()));
        revision.setCosecha(cosecha);

        // Resolver supervisor por ID
        Usuario supervisor = usuarioRepository.findById(dto.getIdSupervisor())
                .orElseThrow(() -> new RuntimeException("Usuario supervisor no encontrado con ID: " + dto.getIdSupervisor()));
        revision.setSupervisor(supervisor);

        revision.setCumpleRequisitos(dto.getCumpleRequisitos());
        revision.setObservaciones(dto.getObservaciones());
        revision.setFecha(dto.getFecha());
        revision.setEstado(true);
        revision.setCreatedAt(LocalDateTime.now(ZONA_LIMA));

        // HU6: Si no cumple requisitos, se notifica automáticamente al encargado
        if (!Boolean.TRUE.equals(dto.getCumpleRequisitos())) {
            revision.setNotificado(true);
        }
        return repository.save(revision);
    }

    @Override
    public RevisionCalidad editar(Long id, RevisionCalidadDTO dto) {
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new RuntimeException(
        "La fecha de revisión no puede ser futura"
            );
        }

        if (
            !Boolean.TRUE.equals(dto.getCumpleRequisitos()) &&
            (
                dto.getObservaciones() == null ||
                dto.getObservaciones().trim().isEmpty()
            )
        ) {
            throw new RuntimeException(
        "Las observaciones son obligatorias cuando la revisión es rechazada"
            );
        }
        return repository.findById(id).map(r -> {
            // Resolver cosecha por ID
            if (dto.getIdCosecha() != null) {
                Cosecha cosecha = cosechaRepository.findById(dto.getIdCosecha())
                        .orElseThrow(() -> new RuntimeException("Cosecha no encontrada con ID: " + dto.getIdCosecha()));
                r.setCosecha(cosecha);
            }

            // Resolver supervisor por ID
            if (dto.getIdSupervisor() != null) {
                Usuario supervisor = usuarioRepository.findById(dto.getIdSupervisor())
                        .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + dto.getIdSupervisor()));
                r.setSupervisor(supervisor);
            }

            r.setCumpleRequisitos(dto.getCumpleRequisitos());
            r.setObservaciones(dto.getObservaciones());
            r.setFecha(dto.getFecha());

            // Si no cumple, notificar
            if (!Boolean.TRUE.equals(dto.getCumpleRequisitos())) {
                r.setNotificado(true);
            }
            r.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(r);
        }).orElseThrow(() -> new RuntimeException("Revisión de calidad no encontrada con ID: " + id));
    }

    @Override
    public RevisionCalidad eliminar(Long id) {
        return repository.findById(id).map(r -> {
            r.setEstado(false);
            r.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(r);
        }).orElse(null);
    }

    @Override
    public RevisionCalidad restaurar(Long id) {
        return repository.findById(id).map(r -> {
            r.setEstado(true);
            r.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(r);
        }).orElse(null);
    }
}
