package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.model.AlertaFitosanitaria;
import vallegrande.edu.pe.arona.model.Campo;
import vallegrande.edu.pe.arona.model.Cultivo;
import vallegrande.edu.pe.arona.model.Usuario;
import vallegrande.edu.pe.arona.dto.AlertaFitosanitariaDTO;
import vallegrande.edu.pe.arona.repository.AlertaFitosanitariaRepository;
import vallegrande.edu.pe.arona.repository.CampoRepository;
import vallegrande.edu.pe.arona.repository.CultivoRepository;
import vallegrande.edu.pe.arona.repository.UsuarioRepository;
import vallegrande.edu.pe.arona.service.AlertaFitosanitariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class AlertaFitosanitariaServiceImpl implements AlertaFitosanitariaService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private AlertaFitosanitariaRepository repository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private CultivoRepository cultivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AlertaFitosanitaria> listarTodos() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertaFitosanitaria> listarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaFitosanitaria> listarPendientes() {
        return repository.findByEstadoAlerta("PENDIENTE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaFitosanitaria> listarPorCampo(Long idCampo) {
        return repository.findByIdCampo(idCampo);
    }

    @Override
    @Transactional
    public AlertaFitosanitaria crear(AlertaFitosanitariaDTO dto) {
        AlertaFitosanitaria alerta = new AlertaFitosanitaria();

        // Resolver y validar campo (debe existir y estar activo)
        Campo campo = campoRepository.findById(dto.getIdCampo())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Campo no encontrado con ID: " + dto.getIdCampo()));
        if (Boolean.FALSE.equals(campo.getEstado())) {
            throw new IllegalArgumentException(
                    "El campo con ID " + dto.getIdCampo() + " está inactivo y no puede usarse.");
        }
        alerta.setCampo(campo);

        // Resolver cultivo por ID (opcional, pero si se provee debe existir y estar activo)
        if (dto.getIdCultivo() != null) {
            Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Cultivo no encontrado con ID: " + dto.getIdCultivo()));
            if (Boolean.FALSE.equals(cultivo.getEstado())) {
                throw new IllegalArgumentException(
                        "El cultivo con ID " + dto.getIdCultivo() + " está inactivo y no puede usarse.");
            }
            alerta.setCultivo(cultivo);
        }

        // Resolver y validar usuario que reporta (debe existir y estar activo)
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuarioReporta())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario no encontrado con ID: " + dto.getIdUsuarioReporta()));
        if (Boolean.FALSE.equals(usuario.getEstado())) {
            throw new IllegalArgumentException(
                    "El usuario con ID " + dto.getIdUsuarioReporta() + " está inactivo.");
        }
        alerta.setUsuarioReporta(usuario);

        alerta.setDescripcionProblema(dto.getDescripcionProblema());
        alerta.setTipoProblema(dto.getTipoProblema());
        alerta.setFechaDeteccion(dto.getFechaDeteccion());
        alerta.setEstadoAlerta("PENDIENTE");
        alerta.setEstado(true);
        alerta.setCreatedAt(LocalDateTime.now(ZONA_LIMA));
        return repository.save(alerta);
    }

    @Override
    @Transactional
    public AlertaFitosanitaria editar(Long id, AlertaFitosanitariaDTO dto) {
        return repository.findById(id).map(a -> {
            // Resolver y validar campo
            if (dto.getIdCampo() != null) {
                Campo campo = campoRepository.findById(dto.getIdCampo())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Campo no encontrado con ID: " + dto.getIdCampo()));
                if (Boolean.FALSE.equals(campo.getEstado())) {
                    throw new IllegalArgumentException(
                            "El campo con ID " + dto.getIdCampo() + " está inactivo y no puede usarse.");
                }
                a.setCampo(campo);
            }

            // Resolver cultivo por ID (opcional)
            if (dto.getIdCultivo() != null) {
                Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Cultivo no encontrado con ID: " + dto.getIdCultivo()));
                if (Boolean.FALSE.equals(cultivo.getEstado())) {
                    throw new IllegalArgumentException(
                            "El cultivo con ID " + dto.getIdCultivo() + " está inactivo y no puede usarse.");
                }
                a.setCultivo(cultivo);
            } else {
                a.setCultivo(null);
            }

            // Resolver y validar usuario que reporta
            if (dto.getIdUsuarioReporta() != null) {
                Usuario usuario = usuarioRepository.findById(dto.getIdUsuarioReporta())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Usuario no encontrado con ID: " + dto.getIdUsuarioReporta()));
                if (Boolean.FALSE.equals(usuario.getEstado())) {
                    throw new IllegalArgumentException(
                            "El usuario con ID " + dto.getIdUsuarioReporta() + " está inactivo.");
                }
                a.setUsuarioReporta(usuario);
            }

            a.setDescripcionProblema(dto.getDescripcionProblema());
            a.setTipoProblema(dto.getTipoProblema());
            a.setFechaDeteccion(dto.getFechaDeteccion());
            a.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public AlertaFitosanitaria atender(Long id, String solucionAplicada) {
        if (solucionAplicada == null || solucionAplicada.isBlank()) {
            throw new IllegalArgumentException("La solución aplicada no puede estar vacía.");
        }
        return repository.findById(id).map(a -> {
            if ("ATENDIDO".equals(a.getEstadoAlerta())) {
                throw new IllegalStateException("La alerta con ID " + id + " ya fue atendida.");
            }
            a.setEstadoAlerta("ATENDIDO");
            a.setSolucionAplicada(solucionAplicada);
            a.setFechaResolucion(LocalDate.now(ZONA_LIMA));
            a.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public AlertaFitosanitaria eliminar(Long id) {
        return repository.findById(id).map(a -> {
            a.setEstado(false);
            a.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElse(null);
    }

    @Override
    @Transactional
    public AlertaFitosanitaria restaurar(Long id) {
        return repository.findById(id).map(a -> {
            a.setEstado(true);
            a.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(a);
        }).orElse(null);
    }
}
